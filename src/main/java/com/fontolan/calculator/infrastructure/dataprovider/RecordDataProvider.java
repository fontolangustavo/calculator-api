package com.fontolan.calculator.infrastructure.dataprovider;

import com.fontolan.calculator.domain.model.Record;

public interface RecordDataProvider {
    void save(Record record);
}
