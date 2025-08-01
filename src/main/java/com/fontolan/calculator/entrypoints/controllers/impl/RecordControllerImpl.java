package com.fontolan.calculator.entrypoints.controllers.impl;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.entrypoints.controllers.RecordController;
import com.fontolan.calculator.entrypoints.response.RecordResponse;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.fontolan.calculator.infrastructure.logging.LoggerUtil.getLogger;

@RestController
public class RecordControllerImpl implements RecordController {

    private static final Logger log = getLogger(RecordControllerImpl.class);

    @Override
    public ResponseEntity<List<RecordResponse>> getUserRecords() {
        log.info("[Record] Fetching mocked record list");

        RecordResponse mock = new RecordResponse();
        mock.setId(UUID.randomUUID());
        mock.setOperationType(OperationType.ADDITION);
        mock.setAmount("5 + 10");
        mock.setUserBalance(BigDecimal.valueOf(100));
        mock.setOperationResponse("15");
        mock.setCreatedAt(LocalDateTime.now());

        return ResponseEntity.ok(List.of(mock));
    }

    @Override
    public ResponseEntity<Void> softDeleteRecord(UUID id) {
        log.warn("[Record] Soft deleting mocked record with id: {}", id);
        return ResponseEntity.noContent().build();
    }
}
