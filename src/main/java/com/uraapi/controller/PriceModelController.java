package com.uraapi.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uraapi.service.PriceRegressionService;

@RestController
@RequestMapping("/api/model")
public class PriceModelController {

    @Autowired
    private PriceRegressionService regressionService;

    /**
     * 用数据库里现有的全部交易记录训练一次模型。
     * 每次抓了新数据之后，可以重新调这个接口，让模型用上最新数据。
     * curl -X POST "http://localhost:8080/api/model/train"
     */
    @PostMapping("/train")
    public Object train() {
        var result = regressionService.train();
        return Map.of(
                "message", "训练完成",
                "使用样本数", result.sampleSize(),
                "R平方拟合优度", result.rSquared(),
                "截距", result.intercept(),
                "面积系数", result.floorAreaCoefficient(),
                "楼层系数", result.storeyCoefficient(),
                "成熟组屋区系数", result.matureEstateCoefficient()
        );
    }

    /**
     * 用训练好的模型预测价格，必须先调用过 /train。
     * curl "http://localhost:8080/api/model/predict?floorAreaSqm=90&storeyRange=07 TO 09&town=BEDOK"
     */
    @GetMapping("/predict")
    public Object predict(
            @RequestParam double floorAreaSqm,
            @RequestParam String storeyRange,
            @RequestParam String town
    ) {
        double predictedPrice = regressionService.predict(floorAreaSqm, storeyRange, town);
        return Map.of(
                "输入面积", floorAreaSqm,
                "输入楼层区间", storeyRange,
                "输入区域", town,
                "AI预测均价", Math.round(predictedPrice)
        );
    }
}