package com.uraapi.service;

import com.uraapi.entity.DailyMerkleRoot;
import com.uraapi.entity.ResaleTransaction;
import com.uraapi.repository.DailyMerkleRootRepository;
import com.uraapi.repository.ResaleTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MerkleTreeService {

    @Autowired
    private ResaleTransactionRepository transactionRepository;

    @Autowired
    private DailyMerkleRootRepository merkleRootRepository;

    public BuildResult buildRootForDate(LocalDate date) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        List<ResaleTransaction> transactions = fetchTransactionsOfDate(targetDate);

        if (transactions.isEmpty()) {
            throw new IllegalStateException("日期 " + targetDate + " 没有任何抓取到的交易记录，无法打包");
        }

        List<String> leafHashes = new ArrayList<>();
        for (ResaleTransaction tx : transactions) {
            leafHashes.add(computeLeafHash(tx));
        }

        List<List<String>> levels = buildTreeLevels(leafHashes);
        String rootHash = levels.get(levels.size() - 1).get(0);

        DailyMerkleRoot record = merkleRootRepository.findByRootDate(targetDate)
                .orElse(new DailyMerkleRoot());
        record.setRootDate(targetDate);
        record.setRootHash(rootHash);
        record.setTransactionCount(transactions.size());
        record.setCreatedAt(LocalDateTime.now());
        merkleRootRepository.save(record);

        return new BuildResult(targetDate, rootHash, transactions.size());
    }

    public VerifyResult verifyTransaction(Long sourceId) {
        ResaleTransaction tx = transactionRepository.findBySourceId(sourceId);
        if (tx == null) {
            throw new IllegalArgumentException("找不到 sourceId=" + sourceId + " 这笔交易");
        }

        LocalDate txDate = tx.getFetchedAt().toLocalDate();
        Optional<DailyMerkleRoot> rootRecord = merkleRootRepository.findByRootDate(txDate);
        if (rootRecord.isEmpty()) {
            throw new IllegalStateException(
                    "这笔交易所在的日期 " + txDate + " 还没建过 Merkle Tree，先调用 /api/blockchain/build");
        }

        List<ResaleTransaction> sameDayTransactions = fetchTransactionsOfDate(txDate);
        List<String> leafHashes = new ArrayList<>();
        int targetIndex = -1;
        for (int i = 0; i < sameDayTransactions.size(); i++) {
            String leaf = computeLeafHash(sameDayTransactions.get(i));
            leafHashes.add(leaf);
            if (sameDayTransactions.get(i).getSourceId().equals(sourceId)) {
                targetIndex = i;
            }
        }

        List<List<String>> levels = buildTreeLevels(leafHashes);
        List<ProofStep> proof = buildProof(levels, targetIndex);

        String recomputedLeaf = leafHashes.get(targetIndex);
        String recomputedRoot = applyProof(recomputedLeaf, proof);
        String storedRoot = rootRecord.get().getRootHash();

        boolean valid = recomputedRoot.equals(storedRoot);

        return new VerifyResult(sourceId, txDate, recomputedLeaf, storedRoot, recomputedRoot, valid, proof);
    }

    private List<ResaleTransaction> fetchTransactionsOfDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return transactionRepository.findByFetchedAtBetweenOrderBySourceIdAsc(start, end);
    }

    private String computeLeafHash(ResaleTransaction tx) {
        String raw = tx.getSourceId() + "|" + tx.getMonth() + "|" + tx.getTown() + "|"
                + tx.getFlatType() + "|" + tx.getBlock() + "|" + tx.getStreetName() + "|"
                + tx.getStoreyRange() + "|" + tx.getFloorAreaSqm() + "|" + tx.getFlatModel() + "|"
                + tx.getLeaseCommenceDate() + "|" + tx.getRemainingLease() + "|" + tx.getResalePrice();
        return sha256Hex(raw);
    }

    private List<List<String>> buildTreeLevels(List<String> leafHashes) {
        List<List<String>> levels = new ArrayList<>();
        levels.add(leafHashes);

        List<String> current = leafHashes;
        while (current.size() > 1) {
            List<String> next = new ArrayList<>();
            for (int i = 0; i < current.size(); i += 2) {
                String left = current.get(i);
                String right = (i + 1 < current.size()) ? current.get(i + 1) : current.get(i);
                next.add(sha256Hex(left + right));
            }
            levels.add(next);
            current = next;
        }
        return levels;
    }

    private List<ProofStep> buildProof(List<List<String>> levels, int leafIndex) {
        List<ProofStep> proof = new ArrayList<>();
        int index = leafIndex;

        for (int level = 0; level < levels.size() - 1; level++) {
            List<String> currentLevel = levels.get(level);
            boolean isLeftNode = (index % 2 == 0);
            int siblingIndex = isLeftNode ? index + 1 : index - 1;

            String siblingHash;
            String position;
            if (isLeftNode) {
                siblingHash = (siblingIndex < currentLevel.size())
                        ? currentLevel.get(siblingIndex) : currentLevel.get(index);
                position = "right";
            } else {
                siblingHash = currentLevel.get(siblingIndex);
                position = "left";
            }

            proof.add(new ProofStep(siblingHash, position));
            index = index / 2;
        }
        return proof;
    }

    private String applyProof(String leafHash, List<ProofStep> proof) {
        String current = leafHash;
        for (ProofStep step : proof) {
            if ("right".equals(step.position())) {
                current = sha256Hex(current + step.siblingHash());
            } else {
                current = sha256Hex(step.siblingHash() + current);
            }
        }
        return current;
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 算法不可用，理论上不应该发生", e);
        }
    }

    public record BuildResult(LocalDate date, String rootHash, int transactionCount) {
    }

    public record ProofStep(String siblingHash, String position) {
    }

    public record VerifyResult(
            Long sourceId,
            LocalDate date,
            String recomputedLeafHash,
            String storedRootHash,
            String recomputedRootHash,
            boolean valid,
            List<ProofStep> proof
    ) {
    }
}
