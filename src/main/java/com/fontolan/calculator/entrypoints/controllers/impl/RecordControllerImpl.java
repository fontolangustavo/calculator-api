package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.application.usecases.record.DeleteRecordUseCase;
import com.fontolan.calculator.application.usecases.record.GetUserRecordsUseCase;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.controllers.RecordController;
import com.fontolan.calculator.entrypoints.mapper.RecordMapper;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.entrypoints.response.RecordResponse;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class RecordControllerImpl implements RecordController {

    private static final Logger log = getLogger(RecordControllerImpl.class);
    private final GetUserRecordsUseCase getUserRecordsUseCase;
    private final DeleteRecordUseCase deleteRecordUseCase;
    private final RecordMapper recordMapper;

    public RecordControllerImpl(GetUserRecordsUseCase getUserRecordsUseCase, DeleteRecordUseCase deleteRecordUseCase, RecordMapper recordMapper) {
        this.getUserRecordsUseCase = getUserRecordsUseCase;
        this.deleteRecordUseCase = deleteRecordUseCase;
        this.recordMapper = recordMapper;
    }

    @Override
    public ResponseEntity<Page<RecordResponse>> getUserRecords(RecordFilterRequest request) {
        // TODO: trocar por authenticated user futuramente
        String username = "teste";
        Page<Record> response = getUserRecordsUseCase.execute(request, username);

        Page<RecordResponse> responses = response.map(recordMapper::toResponse);

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<Void> softDeleteRecord(UUID id) {
        log.warn("[Record] Soft deleting mocked record with id: {}", id);

        // TODO: trocar por authenticated user futuramente
        String username = "teste";

        deleteRecordUseCase.execute(id, username);

        return ResponseEntity.noContent().build();
    }
}
