package com.uraapi.repository;

import com.uraapi.entity.ResaleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ResaleTransactionRepository extends JpaRepository<ResaleTransaction, Long> {

    boolean existsBySourceId(Long sourceId);

    ResaleTransaction findBySourceId(Long sourceId);

    List<ResaleTransaction> findByTownIgnoreCase(String town);

    List<ResaleTransaction> findTop50ByOrderByFetchedAtDesc();

    List<ResaleTransaction> findByFetchedAtBetweenOrderBySourceIdAsc(LocalDateTime start, LocalDateTime end);

    List<ResaleTransaction> findByBlockAndStreetNameIgnoreCaseAndFlatTypeIgnoreCaseOrderByMonthAsc(
            String block, String streetName, String flatType);
}
