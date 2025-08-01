package com.fontolan.calculator.entrypoints.mapper;

import com.fontolan.calculator.entrypoints.response.RecordResponse;

public interface RecordMapper {
    RecordResponse toResponse(Record record);
}
