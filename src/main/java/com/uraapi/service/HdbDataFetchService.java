package com.uraapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uraapi.entity.ResaleTransaction;
import com.uraapi.repository.ResaleTransactionRepository;

/**
 * 第一步的核心：从 data.gov.sg 拉取 HDB 转售交易数据，存进本地 H2 数据库。
 * 数据集：Resale flat prices based on registration date from Jan-2017 onwards
 * 免费、公开、合法，不需要任何 API Key。
 */
@Service
public class HdbDataFetchService {

    private static final Logger log = LoggerFactory.getLogger(HdbDataFetchService.class);

    // 这个 dataset_id 是官方固定的，对应"2017年至今的HDB转售数据"这个数据集
    private static final String DATASET_ID = "d_8b84c4ee58e3cfc0ece0d773c8ca6abc";
    private static final String API_URL = "https://data.gov.sg/api/action/datastore_search";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ResaleTransactionRepository repository;

    // 每天定时任务一次抓多少条，写在配置文件里，不用改代码就能调整
    @Value("${app.fetch.daily-batch-size:500}")
    private int dailyBatchSize;

    /**
     * 每天凌晨 2 点自动跑一次抓取。
     * cron 表达式 "0 0 2 * * *" 的意思：秒=0 分=0 时=2，每天都跑。
     * 这个方法不用你手动调用，Spring 会自己在后台按时触发。
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledDailyFetch() {
        log.info("【定时任务】开始每日自动抓取，本次目标 {} 条", dailyBatchSize);
        int saved = fetchNextBatch(dailyBatchSize);
        log.info("【定时任务】本次抓取完成，新增 {} 条，数据库当前总条数 {}", saved, repository.count());
    }

    /**
     * 抓取"下一批"记录：自动用数据库里当前已有的条数作为 offset，
     * 所以每次调用（不管是手动 curl 还是定时任务触发）都会往后翻页抓新的一批，
     * 不会一直重复抓同样的前 N 条。
     *
     * @param limit  这一批想抓多少条
     * @return 本次实际新增到数据库里的条数（已存在的会被跳过，不会重复插入）
     */
    public int fetchNextBatch(int limit) {
        long offset = repository.count();
        String url = API_URL + "?resource_id=" + DATASET_ID + "&limit=" + limit + "&offset=" + offset;

        String rawJson = restTemplate.getForObject(url, String.class);

        List<ResaleTransaction> toSave = new ArrayList<>();
        try {
            JsonNode root = objectMapper.readTree(rawJson);
            JsonNode records = root.path("result").path("records");

            for (JsonNode record : records) {
                Long sourceId = record.path("_id").asLong();

                // 已经存在的记录直接跳过，避免每次重复抓取产生重复数据
                if (repository.existsBySourceId(sourceId)) {
                    continue;
                }

                ResaleTransaction tx = new ResaleTransaction();
                tx.setSourceId(sourceId);
                tx.setMonth(record.path("month").asText(null));
                tx.setTown(record.path("town").asText(null));
                tx.setFlatType(record.path("flat_type").asText(null));
                tx.setBlock(record.path("block").asText(null));
                tx.setStreetName(record.path("street_name").asText(null));
                tx.setStoreyRange(record.path("storey_range").asText(null));
                tx.setFloorAreaSqm(parseDoubleSafe(record.path("floor_area_sqm").asText(null)));
                tx.setFlatModel(record.path("flat_model").asText(null));
                tx.setLeaseCommenceDate(record.path("lease_commence_date").asText(null));
                tx.setRemainingLease(record.path("remaining_lease").asText(null));
                tx.setResalePrice(parseDoubleSafe(record.path("resale_price").asText(null)));
                tx.setFetchedAt(LocalDateTime.now());

                toSave.add(tx);
            }
        } catch (Exception e) {
            throw new RuntimeException("解析 data.gov.sg 返回的 JSON 失败: " + e.getMessage(), e);
        }

        repository.saveAll(toSave);
        return toSave.size();
    }

    private Double parseDoubleSafe(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}