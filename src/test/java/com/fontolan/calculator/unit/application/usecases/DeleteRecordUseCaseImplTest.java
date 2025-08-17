package com.fontolan.calculator.unit.application.usecases;

import com.fontolan.calculator.application.usecases.record.impl.DeleteRecordUseCaseImpl;
import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.enums.UserStatus;
import com.fontolan.calculator.domain.exception.BusinessException;
import com.fontolan.calculator.domain.exception.NotFoundException;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteRecordUseCaseImplTest {

    @InjectMocks
    private DeleteRecordUseCaseImpl useCase;

    @Mock
    private UserDataProvider userDataProvider;
    @Mock
    private RecordDataProvider recordDataProvider;

    private UUID userId;
    private UUID recordId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        recordId = UUID.randomUUID();
    }

    @Test
    void shouldDeleteRecordIfUserIsOwner() {
        Record record = mockRecord();
        record.setId(recordId);
        record.setUserId(userId);
        record.setCreatedAt(Instant.now());
        record.setOperationResponse("5");
        record.setAmount("2+3");
        record.setUserBalance(BigDecimal.valueOf(95));

        when(userDataProvider.findByUsername(anyString())).thenReturn(mockUser());
        when(recordDataProvider.findById(recordId)).thenReturn(record);

        useCase.execute(recordId, anyString());

        verify(recordDataProvider).softDelete(argThat(deleted ->
                deleted.equals(recordId)
        ));
    }

    @Test
    void shouldThrowExceptionIfRecordNotFound() {
        when(userDataProvider.findByUsername(anyString())).thenReturn(mockUser());
        when(recordDataProvider.findById(recordId)).thenThrow(new NotFoundException("Record not found: " + recordId));

        assertThatThrownBy(() -> useCase.execute(recordId, anyString()))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Record not found");
    }

    @Test
    void shouldThrowExceptionIfRecordDoesNotBelongToUser() {
        Record record = mockRecord();
        record.setId(recordId);
        record.setUserId(UUID.randomUUID());

        when(userDataProvider.findByUsername(anyString())).thenReturn(mockUser());
        when(recordDataProvider.findById(recordId)).thenReturn(record);

        assertThatThrownBy(() -> useCase.execute(recordId, anyString()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Record does not belong to the user.");
    }

    private User mockUser() {
        return new User(
                userId,
                "user1",
                "password123",
                UserStatus.ACTIVE,
                BigDecimal.valueOf(100L)
        );
    }

    private Record mockRecord() {
        return new Record(
                recordId,
                userId,
                OperationType.ADDITION,
                "100",
                BigDecimal.valueOf(500),
                "100 + 50 = 150",
                Instant.now(),
                null
        );
    }
}
