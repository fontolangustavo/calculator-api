package com.fontolan.calculator.infrastructure.dataprovider.repository;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.infrastructure.dataprovider.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OperationRepository extends JpaRepository<OperationEntity, Long>, JpaSpecificationExecutor<OperationEntity> {
    Optional<OperationEntity> findByType(OperationType type);
}

