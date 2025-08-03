package com.fontolan.calculator.infrastructure.dataprovider.repository;

import com.fontolan.calculator.infrastructure.dataprovider.entity.RecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.UUID;

public interface RecordRepository extends JpaRepository<RecordEntity, UUID>, JpaSpecificationExecutor<RecordEntity> {
    List<RecordEntity> findByUserIdAndDeletedAtIsNull(UUID userId);
}