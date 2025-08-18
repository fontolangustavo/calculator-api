package com.fontolan.calculator.unit.infrastructure.dataprovider;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.exception.NotFoundException;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RecordDataProviderImplTest {

    private UserRepository userRepository;
    private RecordRepository recordRepository;
    private RecordEntityMapper mapper;
    private RecordDataProviderImpl provider;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        recordRepository = mock(RecordRepository.class);
        mapper = mock(RecordEntityMapper.class);
        provider = new RecordDataProviderImpl(userRepository, recordRepository, mapper);
    }

    @Test
    void save_shouldMapAndPersist() {
        var domain = mock(Record.class);
        var entity = mock(RecordEntity.class);
        when(mapper.toEntity(domain)).thenReturn(entity);

        provider.save(domain);

        verify(mapper).toEntity(domain);
        verify(recordRepository).save(entity);
        verifyNoMoreInteractions(mapper, recordRepository);
    }

    @Test
    void softDelete_shouldSetDeletedAtAndSave() {
        var id = UUID.randomUUID();
        var entity = mock(RecordEntity.class);
        when(recordRepository.findById(id)).thenReturn(Optional.of(entity));

        provider.softDelete(id);

        InOrder inOrder = inOrder(recordRepository, entity, recordRepository);
        inOrder.verify(recordRepository).findById(id);
        inOrder.verify(entity).setDeletedAt(any(Instant.class));
        inOrder.verify(recordRepository).save(entity);
    }

    @Test
    void softDelete_shouldThrowWhenNotFound() {
        var id = UUID.randomUUID();
        when(recordRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> provider.softDelete(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Record not found")
                .hasMessageContaining(id.toString());
        verify(recordRepository).findById(id);
        verifyNoMoreInteractions(recordRepository);
    }

    @Test
    void findByUsername_shouldQueryWithSpecAndPageableAndMap() {
        var username = "MOCK_USER";
        var userId = UUID.randomUUID();
        var user = mock(UserEntity.class);
        when(user.getId()).thenReturn(userId);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        var request = new RecordFilterRequest();
        request.setPage(2);
        request.setSize(5);
        request.setSortBy("createdAt");
        request.setSortDir("DESC");
        request.setType(OperationType.ADDITION);
        request.setStartDate(LocalDate.of(2025, 8, 1));
        request.setEndDate(LocalDate.of(2025, 8, 15));

        var entity1 = mock(RecordEntity.class);
        var entity2 = mock(RecordEntity.class);
        var pageEntities = new PageImpl<>(List.of(entity1, entity2), PageRequest.of(2, 5, Sort.by(Sort.Direction.DESC, "createdAt")), 2);
        when(recordRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(pageEntities);

        var domain1 = mock(Record.class);
        var domain2 = mock(Record.class);
        when(mapper.toDomain(entity1)).thenReturn(domain1);
        when(mapper.toDomain(entity2)).thenReturn(domain2);

        var result = provider.findByUsername(request, username);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<Specification<RecordEntity>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(recordRepository).findAll(specCaptor.capture(), pageableCaptor.capture());

        var pageable = pageableCaptor.getValue();
        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(5);
        assertThat(pageable.getSort()).isEqualTo(Sort.by(Sort.Direction.DESC, "createdAt"));

        assertThat(result.getContent()).containsExactly(domain1, domain2);
        verify(mapper).toDomain(entity1);
        verify(mapper).toDomain(entity2);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_shouldThrowWhenUserNotFound() {
        when(userRepository.findByUsername("x")).thenReturn(Optional.empty());
        var req = new RecordFilterRequest();
        assertThatThrownBy(() -> provider.findByUsername(req, "x"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User not found");
        verify(userRepository).findByUsername("x");
        verifyNoInteractions(recordRepository);
    }

    @Test
    void findById_shouldReturnMappedDomain() {
        var id = UUID.randomUUID();
        var entity = mock(RecordEntity.class);
        var domain = mock(Record.class);
        when(recordRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(domain);

        var result = provider.findById(id);

        assertThat(result).isSameAs(domain);
        verify(recordRepository).findById(id);
        verify(mapper).toDomain(entity);
    }

    @Test
    void findById_shouldThrowWhenNotFound() {
        var id = UUID.randomUUID();
        when(recordRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> provider.findById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Record not found")
                .hasMessageContaining(id.toString());
        verify(recordRepository).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    void byDateRange_nullsShouldNotBreakSpecChain() {
        var username = "MOCK_USER";
        var userId = UUID.randomUUID();
        var user = mock(UserEntity.class);
        when(user.getId()).thenReturn(userId);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        var request = new RecordFilterRequest();
        request.setPage(0);
        request.setSize(10);
        request.setSortBy("createdAt");
        request.setSortDir("ASC");
        request.setType(null);
        request.setStartDate(null);
        request.setEndDate(null);

        when(recordRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(Page.empty());

        var result = provider.findByUsername(request, username);

        assertThat(result.getContent()).isEmpty();
        verify(recordRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}