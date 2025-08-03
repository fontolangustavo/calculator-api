package com.fontolan.calculator.infrastructure.dataprovider.impl;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.entity.RecordEntity;
import com.fontolan.calculator.infrastructure.dataprovider.repository.RecordRepository;
import com.fontolan.calculator.infrastructure.dataprovider.repository.UserRepository;
import com.fontolan.calculator.infrastructure.mapper.RecordEntityMapper;
import jakarta.persistence.criteria.Path;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RecordDataProviderImpl implements RecordDataProvider {
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final RecordEntityMapper recordEntityMapper;

    public RecordDataProviderImpl(UserRepository userRepository, RecordRepository recordRepository, RecordEntityMapper recordEntityMapper) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.recordEntityMapper = recordEntityMapper;
    }

    @Override
    public void save(Record record) {
        recordRepository.save(recordEntityMapper.toEntity(record));
    }

    @Override
    public Page<Record> findByUsername(RecordFilterRequest request, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(
                    Sort.Direction.valueOf(request.getSortDir().toUpperCase()),
                    request.getSortBy()
                )
        );

        Specification<RecordEntity> spec = Specification.allOf(byUser(user.getId()))
                .and(notDeleted())
                .and(byType(request.getType()))
                .and(byDateRange(request.getStartDate(), request.getEndDate()));

        return recordRepository.findAll(spec, pageable)
                .map(recordEntityMapper::toDomain);
    }

    private Specification<RecordEntity> byUser(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    private Specification<RecordEntity> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    private Specification<RecordEntity> byType(OperationType type) {
        if (type == null) return null;
        return (root, query, cb) -> cb.equal(root.get("operationType"), type.name());
    }

    private Specification<RecordEntity> byDateRange(LocalDate start, LocalDate end) {
        if (start == null && end == null) return null;

        return (root, query, cb) -> {
            Path<LocalDateTime> createdAt = root.get("createdAt");

            if (start != null && end != null) {
                return cb.between(createdAt, start.atStartOfDay(), end.atTime(23, 59, 59));
            } else if (start != null) {
                return cb.greaterThanOrEqualTo(createdAt, start.atStartOfDay());
            } else {
                return cb.lessThanOrEqualTo(createdAt, end.atTime(23, 59, 59));
            }
        };
    }
}
