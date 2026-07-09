package com.uraapi.controller;

import com.uraapi.entity.ResaleTransaction;
import com.uraapi.repository.ResaleTransactionRepository;
import com.uraapi.service.HdbDataFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private HdbDataFetchService fetchService;

    @Autowired
    private ResaleTransactionRepository repository;

    @PostMapping("/fetch")
    public Map<String, Object> fetch(@RequestParam(defaultValue = "100") int limit) {
        int savedCount = fetchService.fetchNextBatch(limit);
        long totalInDb = repository.count();
        return Map.of(
                "message", "抓取完成",
                "本次新增条数", savedCount,
                "数据库当前总条数", totalInDb
        );
    }

    @GetMapping("/transactions/recent")
    public List<ResaleTransaction> recent() {
        return repository.findTop50ByOrderByFetchedAtDesc();
    }

    @GetMapping("/transactions")
    public List<ResaleTransaction> byTown(@RequestParam String town) {
        return repository.findByTownIgnoreCase(town);
    }

    @GetMapping("/transactions/chain")
    public Map<String, Object> chain(
            @RequestParam String block,
            @RequestParam String streetName,
            @RequestParam String flatType
    ) {
        List<ResaleTransaction> history = repository
                .findByBlockAndStreetNameIgnoreCaseAndFlatTypeIgnoreCaseOrderByMonthAsc(block, streetName, flatType);

        if (history.isEmpty()) {
            return Map.of(
                    "message", "没有查到匹配的历史交易记录，检查一下 block / streetName / flatType 拼写是否跟数据库里的一致"
            );
        }

        double firstPrice = history.get(0).getResalePrice();
        double lastPrice = history.get(history.size() - 1).getResalePrice();
        double changePercent = ((lastPrice - firstPrice) / firstPrice) * 100.0;

        return Map.of(
                "查询条件", block + " " + streetName + " / " + flatType,
                "历史交易笔数", history.size(),
                "最早成交价", firstPrice,
                "最早成交月份", history.get(0).getMonth(),
                "最近成交价", lastPrice,
                "最近成交月份", history.get(history.size() - 1).getMonth(),
                "累计涨跌幅百分比", Math.round(changePercent * 100) / 100.0,
                "完整交易链", history
        );
    }
}
