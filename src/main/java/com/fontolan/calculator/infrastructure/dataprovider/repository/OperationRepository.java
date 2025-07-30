package com.fontolan.calculator.infrastructure.dataprovider.repository;

import com.fontolan.calculator.infrastructure.dataprovider.entity.OperationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<OperationEntity, Long> {
}

