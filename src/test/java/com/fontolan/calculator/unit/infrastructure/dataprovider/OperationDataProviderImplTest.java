package com.fontolan.calculator.unit.infrastructure.dataprovider;

import com.fontolan.calculator.domain.enums.OperationType;
import com.fontolan.calculator.domain.exception.NotFoundException;
import com.fontolan.calculator.domain.model.Operation;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.infrastructure.dataprovider.entity.OperationEntity;
import com.fontolan.calculator.infrastructure.dataprovider.impl.OperationDataProviderImpl;
import com.fontolan.calculator.infrastructure.dataprovider.repository.OperationRepository;
import com.fontolan.calculator.infrastructure.mapper.OperationEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OperationDataProviderImplTest {

    private OperationRepository operationRepository;
    private OperationEntityMapper operationEntityMapper;
    private OperationDataProviderImpl dataProvider;
    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    @BeforeEach
    void setUp() {
        operationRepository = mock(OperationRepository.class);
        operationEntityMapper = mock(OperationEntityMapper.class);
        dataProvider = new OperationDataProviderImpl(operationRepository, operationEntityMapper);
    }

    @Test
    void shouldReturnOperationWhenTypeExists() {
        OperationType type = OperationType.ADDITION;
        OperationEntity entity = new OperationEntity();
        Operation operation = new Operation(123L, OperationType.MULTIPLICATION, BigDecimal.ONE);

        when(operationRepository.findByType(type)).thenReturn(Optional.of(entity));
        when(operationEntityMapper.toDomain(entity)).thenReturn(operation);

        Operation result = dataProvider.findByType(type);

        assertThat(result).isEqualTo(operation);
        verify(operationRepository).findByType(type);
        verify(operationEntityMapper).toDomain(entity);
    }

    @Test
    void shouldThrowExceptionWhenOperationNotFound() {
        OperationType type = OperationType.SUBTRACTION;

        when(operationRepository.findByType(type)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            dataProvider.findByType(type);
        });

        assertThat(exception.getMessage()).isEqualTo("Operation not found for type: " + type);
    }


    @Test
    void shouldBuildPageableAndMapResults_ASC() {
        OperationFilterRequest request = mockRequest(1, 20, "type", "asc");

        OperationEntity e1 = new OperationEntity();
        e1.setType(OperationType.ADDITION);
        OperationEntity e2 = new OperationEntity();

        Page<OperationEntity> repoPage = new PageImpl<>(List.of(e1, e2));
        when(operationRepository.findAll(any(Pageable.class))).thenReturn(repoPage);

        Operation o1 = mockOperation(123L);
        Operation o2 = mockOperation(321L);
        when(operationEntityMapper.toDomain(e1)).thenReturn(o1);
        when(operationEntityMapper.toDomain(e2)).thenReturn(o2);

        Page<Operation> result = dataProvider.findAll(request);

        verify(operationRepository).findAll(pageableCaptor.capture());
        Pageable pageableUsed = pageableCaptor.getValue();
        assertThat(pageableUsed.getPageNumber()).isEqualTo(1);
        assertThat(pageableUsed.getPageSize()).isEqualTo(20);

        Sort sort = pageableUsed.getSort();
        assertThat(sort.getOrderFor("type")).isNotNull();
        assertThat(sort.getOrderFor("type").getDirection()).isEqualTo(Sort.Direction.ASC);

        assertThat(result.getContent().get(0).getType()).isEqualTo(repoPage.stream().findFirst().get().getType());
        verify(operationEntityMapper, times(1)).toDomain(e1);
        verify(operationEntityMapper, times(1)).toDomain(e2);
    }

    @Test
    void shouldRespectCaseInsensitiveSortDir_DESC() {
        OperationFilterRequest request = mockRequest(0, 10, "type", "DeSc");

        when(operationRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<Operation> result = dataProvider.findAll(request);

        verify(operationRepository).findAll(pageableCaptor.capture());
        Pageable pageableUsed = pageableCaptor.getValue();

        assertThat(pageableUsed.getPageNumber()).isEqualTo(0);
        assertThat(pageableUsed.getPageSize()).isEqualTo(10);
        assertThat(pageableUsed.getSort().getOrderFor("type")).isNotNull();
        assertThat(pageableUsed.getSort().getOrderFor("type").getDirection())
                .isEqualTo(Sort.Direction.DESC);

        assertThat(result.getContent().size()).isEqualTo(0);
    }

    @Test
    void shouldReturnEmptyPageWhenRepositoryReturnsEmpty() {
        OperationFilterRequest request = mockRequest(2, 5, "id", "ASC");

        Page<OperationEntity> emptyRepoPage = new PageImpl<>(List.of(), PageRequest.of(2, 5), 0);
        when(operationRepository.findAll(any(Pageable.class))).thenReturn(emptyRepoPage);

        Page<Operation> result = dataProvider.findAll(request);

        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    void shouldThrowWhenSortDirIsInvalid() {
        OperationFilterRequest request = mockRequest(0, 10, "type", "INVALID");

        assertThatThrownBy(() -> dataProvider.findAll(request))
                .isInstanceOf(IllegalArgumentException.class);

        verifyNoInteractions(operationRepository);
    }

    public Operation mockOperation(Long id) {
        OperationType operationType = OperationType.ADDITION;
        BigDecimal cost = new BigDecimal("1.5");

        return new Operation(id, operationType, cost);
    }

    private OperationFilterRequest mockRequest(int page, int size, String sortBy, String sortDir) {
        OperationFilterRequest req = mock(OperationFilterRequest.class);
        when(req.getPage()).thenReturn(page);
        when(req.getSize()).thenReturn(size);
        lenient().when(req.getSortBy()).thenReturn(sortBy);
        when(req.getSortDir()).thenReturn(sortDir);
        return req;
    }
}
