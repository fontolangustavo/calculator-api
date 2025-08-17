package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.record.impl.GetUserRecordsUseCaseImpl;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GetUserRecordsUseCaseImplTest {

    private RecordDataProvider recordDataProvider;
    private GetUserRecordsUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        recordDataProvider = mock(RecordDataProvider.class);
        useCase = new GetUserRecordsUseCaseImpl(recordDataProvider);
    }

    @Test
    void shouldReturnUserRecords() {
        RecordFilterRequest request = new RecordFilterRequest();
        String username = "testUser";

        Record record = mockRecord();
        Page<Record> expectedPage = new PageImpl<>(List.of(record));

        when(recordDataProvider.findByUsername(request, username)).thenReturn(expectedPage);

        Page<Record> result = useCase.execute(request, username);

        assertThat(result).isEqualTo(expectedPage);
        verify(recordDataProvider).findByUsername(request, username);
    }

    private Record mockRecord() {
        return new Record(
                UUID.randomUUID(),
                UUID.randomUUID(),
                OperationType.ADDITION,
                "100",
                BigDecimal.valueOf(500),
                "100 + 50 = 150",
                Instant.now(),
                null
        );
    }
}
