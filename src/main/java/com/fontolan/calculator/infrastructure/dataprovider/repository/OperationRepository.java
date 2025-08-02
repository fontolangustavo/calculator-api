package com.fontolan.calculator.infrastructure.dataprovider.repository;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.infrastructure.dataprovider.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {
    Optional<OperationEntity> findByType(OperationType type);
}

