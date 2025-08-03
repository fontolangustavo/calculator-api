package com.fontolan.calculator.application.usecases.record.impl;

import com.fontolan.calculator.application.usecases.record.GetUserRecordsUseCase;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class GetUserRecordsUseCaseImpl implements GetUserRecordsUseCase {
    private final RecordDataProvider recordDataProvider;

    public GetUserRecordsUseCaseImpl(RecordDataProvider recordDataProvider) {
        this.recordDataProvider = recordDataProvider;
    }

    @Override
    public Page<Record> execute(RecordFilterRequest request, String username) {
        return recordDataProvider.findByUsername(request, username);
    }
}
