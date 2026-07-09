package com.uraapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uraapi.entity.ResaleTransaction;
import com.uraapi.repository.ResaleTransactionRepository;

/**
 * 多元线性回归模型：根据"面积、楼层、是否成熟组屋区"预测 HDB 转售均价。
 *
 * 用的自变量（X）：
 *   1. floorAreaSqm  - 面积（平方米），数值型，直接用
 *   2. storeyMidpoint - 楼层区间的中位数，比如 "07 TO 09" 转成 8.0
 *   3. isMatureEstate - 是否成熟组屋区，1 或 0（HDB 官方分类，不是我们瞎定的）
 * 因变量（Y）：resalePrice - 成交价
 *
 * 用的算法：OLS 最小二乘法多元线性回归（Apache Commons Math 自带实现）。
 */
@Service
public class PriceRegressionService {

    @Autowired
    private ResaleTransactionRepository repository;

    // HDB 官方认定的 15 个"成熟组屋区"，来源：HDB BTO/SBF 分类标准
    private static final Set<String> MATURE_TOWNS = Set.of(
            "ANG MO KIO", "BEDOK", "BISHAN", "BUKIT MERAH", "BUKIT TIMAH",
            "CENTRAL AREA", "CLEMENTI", "GEYLANG", "KALLANG/WHAMPOA",
            "MARINE PARADE", "PASIR RIS", "QUEENSTOWN", "SERANGOON",
            "TAMPINES", "TOA PAYOH"
    );

    // 训练完成后，回归系数存在这几个字段里，供 predict() 使用
    private volatile double interceptCoef;
    private volatile double floorAreaCoef;
    private volatile double storeyCoef;
    private volatile double matureEstateCoef;
    private volatile double rSquared;
    private volatile int trainedSampleSize;
    private volatile boolean trained = false;

    /**
     * 用数据库里现有的全部交易记录训练模型。
     * 每次调用都会用最新的数据重新训练一遍，覆盖上一次的模型。
     *
     * @return 训练结果摘要（R²、样本量等），方便你判断模型好不好
     */
    public TrainResult train() {
        List<ResaleTransaction> all = repository.findAll();

        List<double[]> xRows = new ArrayList<>();
        List<Double> yValues = new ArrayList<>();

        for (ResaleTransaction tx : all) {
            Double storeyMid = parseStoreyMidpoint(tx.getStoreyRange());
            // 跳过任何一个关键字段缺失的记录，避免脏数据把模型带歪
            if (tx.getFloorAreaSqm() == null || storeyMid == null
                    || tx.getResalePrice() == null || tx.getTown() == null) {
                continue;
            }
            double isMature = MATURE_TOWNS.contains(tx.getTown().toUpperCase()) ? 1.0 : 0.0;
            xRows.add(new double[]{tx.getFloorAreaSqm(), storeyMid, isMature});
            yValues.add(tx.getResalePrice());
        }

        if (xRows.size() < 10) {
            throw new IllegalStateException(
                    "训练数据太少（当前只有 " + xRows.size() + " 条有效记录），至少需要几十条才能训练出有意义的模型。"
                            + "先调用 /api/fetch 多抓一些数据。");
        }

        double[] y = new double[yValues.size()];
        for (int i = 0; i < yValues.size(); i++) {
            y[i] = yValues.get(i);
        }
        double[][] x = xRows.toArray(new double[0][]);

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(y, x); // 默认会自动加截距项，不用自己在 x 里加一列 1

        double[] coefficients = regression.estimateRegressionParameters();
        // coefficients[0] 是截距，[1][2][3] 分别对应 x 的三列，顺序和放入 xRows 时一致
        this.interceptCoef = coefficients[0];
        this.floorAreaCoef = coefficients[1];
        this.storeyCoef = coefficients[2];
        this.matureEstateCoef = coefficients[3];
        this.rSquared = regression.calculateRSquared();
        this.trainedSampleSize = xRows.size();
        this.trained = true;

        return new TrainResult(trainedSampleSize, rSquared, interceptCoef, floorAreaCoef, storeyCoef, matureEstateCoef);
    }

    /**
     * 用训练好的模型预测某套房子的价格。
     * 必须先调用过 train()，否则会抛异常提醒你先训练。
     */
    public double predict(double floorAreaSqm, String storeyRange, String town) {
        if (!trained) {
            throw new IllegalStateException("模型还没训练过，先调用 POST /api/model/train");
        }
        Double storeyMid = parseStoreyMidpoint(storeyRange);
        if (storeyMid == null) {
            throw new IllegalArgumentException("楼层区间格式看不懂，期望类似 \"07 TO 09\" 这种格式");
        }
        double isMature = MATURE_TOWNS.contains(town.toUpperCase()) ? 1.0 : 0.0;

        return interceptCoef
                + floorAreaCoef * floorAreaSqm
                + storeyCoef * storeyMid
                + matureEstateCoef * isMature;
    }

    public boolean isTrained() {
        return trained;
    }

    /**
     * 把 "07 TO 09" 这种楼层区间字符串转成中位数 8.0，方便当数值型特征用。
     * 解析不了就返回 null（调用方会跳过这条脏数据）。
     */
    private Double parseStoreyMidpoint(String storeyRange) {
        if (storeyRange == null || !storeyRange.contains("TO")) {
            return null;
        }
        try {
            String[] parts = storeyRange.split("TO");
            double low = Double.parseDouble(parts[0].trim());
            double high = Double.parseDouble(parts[1].trim());
            return (low + high) / 2.0;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 训练结果摘要，用来返回给前端/controller 展示。
     */
    public record TrainResult(
            int sampleSize,
            double rSquared,
            double intercept,
            double floorAreaCoefficient,
            double storeyCoefficient,
            double matureEstateCoefficient
    ) {
    }
}