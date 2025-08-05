package com.fontolan.calculator.unit.entrypoints.controllers;

import com.fontolan.calculator.application.usecases.record.DeleteRecordUseCase;
import com.fontolan.calculator.application.usecases.record.GetUserRecordsUseCase;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.controllers.impl.RecordControllerImpl;
import com.fontolan.calculator.entrypoints.mapper.RecordMapper;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.entrypoints.response.RecordResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordControllerImplTest {

    @InjectMocks
    private RecordControllerImpl controller;

    @Mock
    private GetUserRecordsUseCase getUserRecordsUseCase;
    @Mock
    private DeleteRecordUseCase deleteRecordUseCase;
    @Mock
    private RecordMapper recordMapper;

    @Test
    void shouldReturnPageOfRecordsSuccessfully() {
        RecordFilterRequest request = new RecordFilterRequest();
        request.setPage(0);
        request.setSize(2);
        request.setSortBy("createdAt");
        request.setSortDir("desc");

        Record response1 = mockRecord();
        response1.setId(UUID.randomUUID());

        Record response2 = mockRecord();
        response2.setId(UUID.randomUUID());

        Page<Record> page = new PageImpl<>(List.of(response1, response2));

        when(getUserRecordsUseCase.execute(any(RecordFilterRequest.class), anyString()))
                .thenReturn(page);

        ResponseEntity<Page<RecordResponse>> result = controller.getUserRecords("MOCK_USERNAME", request);

        assertNotNull(result);
        assertEquals(2, result.getBody().getContent().size());
        verify(getUserRecordsUseCase).execute(any(RecordFilterRequest.class), anyString());
    }

    @Test
    void shouldDeleteRecord() {
        UUID recordId = UUID.randomUUID();

        controller.softDeleteRecord("MOCK_USERNAME", recordId);

        verify(deleteRecordUseCase).execute(recordId, "MOCK_USERNAME");
    }

    private Record mockRecord() {
        return new Record(UUID.randomUUID(), UUID.randomUUID(), OperationType.ADDITION, "5 + 4", BigDecimal.TEN, "9", LocalDateTime.now(), null);
    }
}
