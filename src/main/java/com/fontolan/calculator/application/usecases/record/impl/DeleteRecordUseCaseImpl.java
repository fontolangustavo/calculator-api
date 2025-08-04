package com.fontolan.calculator.application.usecases.record.impl;

import com.fontolan.calculator.application.usecases.record.DeleteRecordUseCase;
import com.fontolan.calculator.domain.exception.BusinessException;
import com.fontolan.calculator.domain.model.Record;
import com.fontolan.calculator.domain.model.User;
import com.fontolan.calculator.infrastructure.dataprovider.RecordDataProvider;
import com.fontolan.calculator.infrastructure.dataprovider.UserDataProvider;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteRecordUseCaseImpl implements DeleteRecordUseCase {

    private final UserDataProvider userDataProvider;
    private final RecordDataProvider recordDataProvider;

    public DeleteRecordUseCaseImpl(UserDataProvider userDataProvider, RecordDataProvider recordDataProvider) {
        this.userDataProvider = userDataProvider;
        this.recordDataProvider = recordDataProvider;
    }

    @Override
    public void execute(UUID recordId, String username) {
        User user = userDataProvider.findByUsername(username);
        Record record = recordDataProvider.findById(recordId);

        if (!record.getUserId().equals(user.getId())) {
            throw new BusinessException("Record does not belong to the user.");
        }

        recordDataProvider.softDelete(recordId);
    }
}
