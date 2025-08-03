package com.fontolan.calculator.infrastructure.dataprovider;

import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import org.springframework.data.domain.Page;

public interface RecordDataProvider {
    void save(Record record);
    Page<Record> findByUsername(RecordFilterRequest request, String username);
}
