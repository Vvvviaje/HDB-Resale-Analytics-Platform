package com.uraapi.repository;

import com.uraapi.entity.DailyMerkleRoot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyMerkleRootRepository extends JpaRepository<DailyMerkleRoot, Long> {

    Optional<DailyMerkleRoot> findByRootDate(LocalDate rootDate);
}
