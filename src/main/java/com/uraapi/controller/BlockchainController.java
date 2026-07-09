package com.uraapi.controller;

import com.uraapi.service.MerkleTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    @Autowired
    private MerkleTreeService merkleTreeService;

    @PostMapping("/build")
    public Object build(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        var result = merkleTreeService.buildRootForDate(date);
        return Map.of(
                "message", "Merkle Tree 打包完成",
                "日期", result.date().toString(),
                "打包交易笔数", result.transactionCount(),
                "RootHash", result.rootHash()
        );
    }

    @GetMapping("/verify/{sourceId}")
    public Object verify(@PathVariable Long sourceId) {
        var result = merkleTreeService.verifyTransaction(sourceId);
        return Map.of(
                "交易sourceId", result.sourceId(),
                "所属日期", result.date().toString(),
                "验证结果", result.valid() ? "✅ 数据完整，未被篡改" : "❌ 数据被篡改或不一致",
                "重新计算的叶子哈希", result.recomputedLeafHash(),
                "链上存储的RootHash", result.storedRootHash(),
                "沿证明路径重算出的RootHash", result.recomputedRootHash(),
                "MerkleProof路径长度", result.proof().size()
        );
    }
}
