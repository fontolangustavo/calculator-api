package com.fontolan.calculator.infrastructure.dataprovider.repository;

import com.fontolan.calculator.infrastructure.dataprovider.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecordRepository extends JpaRepository<RecordEntity, UUID> {
    List<RecordEntity> findByUserIdAndDeletedAtIsNull(UUID userId);
}