package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.entrypoints.response.RecordResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/v1/records")
public interface RecordController {

    @GetMapping
    ResponseEntity<Page<RecordResponse>> getUserRecords(@AuthenticationPrincipal String username,
                                                        @Valid RecordFilterRequest request
    );

    @DeleteMapping("/{id}")
    ResponseEntity<Void> softDeleteRecord(@AuthenticationPrincipal String username,
                                          @PathVariable UUID id
    );
}
