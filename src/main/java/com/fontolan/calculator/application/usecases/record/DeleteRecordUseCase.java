package com.fontolan.calculator.application.usecases.record;

import java.util.UUID;

public interface DeleteRecordUseCase {
    void execute(UUID recordId, String username);
}
