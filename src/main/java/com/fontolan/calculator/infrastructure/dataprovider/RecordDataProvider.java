package com.fontolan.calculator.infrastructure.dataprovider;

import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface RecordDataProvider {
    Page<Record> findByUsername(RecordFilterRequest request, String username);
    Record findById(UUID id);
    void save(Record record);
    void softDelete(UUID id);
}
