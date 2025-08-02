package com.fontolan.calculator.infrastructure.dataprovider.impl;

import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.repository.RecordRepository;
import com.fontolan.calculator.infrastructure.mapper.RecordEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class RecordDataProviderImpl implements RecordDataProvider {
    private final RecordRepository recordRepository;
    private final RecordEntityMapper recordEntityMapper;

    public RecordDataProviderImpl(RecordRepository recordRepository, RecordEntityMapper recordEntityMapper) {
        this.recordRepository = recordRepository;
        this.recordEntityMapper = recordEntityMapper;
    }

    @Override
    public void save(Record record) {
        recordRepository.save(recordEntityMapper.toEntity(record));
    }
}
