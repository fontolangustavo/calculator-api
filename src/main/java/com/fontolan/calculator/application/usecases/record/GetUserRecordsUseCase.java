package com.fontolan.calculator.application.usecases.record;

import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import org.springframework.data.domain.Page;

public interface GetUserRecordsUseCase {
    Page<Record> execute(RecordFilterRequest request, String username);
}
