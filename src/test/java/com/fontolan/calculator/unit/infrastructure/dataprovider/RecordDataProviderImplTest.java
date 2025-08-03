package com.fontolan.calculator.unit.infrastructure.dataprovider;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.entity.RecordEntity;
import com.fontolan.calculator.infrastructure.dataprovider.entity.UserEntity;
import com.fontolan.calculator.infrastructure.dataprovider.impl.RecordDataProviderImpl;
import com.fontolan.calculator.infrastructure.dataprovider.repository.RecordRepository;
import com.fontolan.calculator.infrastructure.dataprovider.repository.UserRepository;
import com.fontolan.calculator.infrastructure.mapper.RecordEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecordDataProviderImplTest {

    private UserRepository userRepository;
    private RecordRepository recordRepository;
    private RecordEntityMapper recordEntityMapper;
    private RecordDataProviderImpl dataProvider;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        recordRepository = mock(RecordRepository.class);
        recordEntityMapper = mock(RecordEntityMapper.class);
        dataProvider = new RecordDataProviderImpl(userRepository, recordRepository, recordEntityMapper);
    }

    @Test
    void shouldSaveRecord() {
        Record domain = mockRecord();
        RecordEntity entity = new RecordEntity();

        when(recordEntityMapper.toEntity(domain)).thenReturn(entity);

        dataProvider.save(domain);

        verify(recordRepository).save(entity);
    }

    @Test
    void shouldFindByUsernameWithFilter() {
        String username = "testuser";
        RecordFilterRequest request = new RecordFilterRequest();
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("createdAt");
        request.setSortDir("DESC");
        request.setStartDate(LocalDate.now().minusDays(7));
        request.setEndDate(LocalDate.now());
        request.setType(OperationType.ADDITION);

        var userEntity = new UserEntity();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity));
        RecordEntity recordEntity = new RecordEntity();
        recordEntity.setId(UUID.randomUUID());

        Page<RecordEntity> pageEntity = new PageImpl<>(List.of(recordEntity));
        when(recordRepository.findAll(ArgumentMatchers.<Specification<RecordEntity>>any(), any(Pageable.class))).thenReturn(pageEntity);

        Record record = mockRecord();
        when(recordEntityMapper.toDomain(recordEntity)).thenReturn(record);

        Page<Record> result = dataProvider.findByUsername(request, username);

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(record);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        RecordFilterRequest request = new RecordFilterRequest();
        String username = "unknown";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class, () -> {
            dataProvider.findByUsername(request, username);
        });
    }
    @Test
    void shouldSoftDeleteRecordWhenExists() {
        UUID recordId = UUID.randomUUID();
        RecordEntity entity = new RecordEntity();
        entity.setId(recordId);
        entity.setDeletedAt(null);

        when(recordRepository.findById(recordId)).thenReturn(Optional.of(entity));

        dataProvider.softDelete(recordId);

        verify(recordRepository).save(argThat(saved ->
                saved.getId().equals(recordId) &&
                        saved.getDeletedAt() != null
        ));
    }

    @Test
    void shouldThrowExceptionWhenRecordNotFound() {
        UUID recordId = UUID.randomUUID();
        when(recordRepository.findById(recordId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataProvider.softDelete(recordId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Record not found");
    }

    @Test
    void shouldReturnRecordWhenEntityExists() {
        UUID recordId = UUID.randomUUID();
        RecordEntity entity = new RecordEntity();
        entity.setId(recordId);

        Record domain = mockRecord();
        domain.setId(recordId);

        when(recordRepository.findById(recordId)).thenReturn(Optional.of(entity));
        when(recordEntityMapper.toDomain(entity)).thenReturn(domain);

        Record result = dataProvider.findById(recordId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(recordId);
        verify(recordRepository).findById(recordId);
        verify(recordEntityMapper).toDomain(entity);
    }

    @Test
    void shouldThrowExceptionWhenEntityNotFound() {
        UUID recordId = UUID.randomUUID();
        when(recordRepository.findById(recordId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataProvider.findById(recordId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Record not found");
    }

    private Record mockRecord() {
        return new Record(
                UUID.randomUUID(),
                UUID.randomUUID(),
                OperationType.ADDITION,
                "100",
                BigDecimal.valueOf(500),
                "100 + 50 = 150",
                LocalDateTime.now(),
                null
        );
    }
}
