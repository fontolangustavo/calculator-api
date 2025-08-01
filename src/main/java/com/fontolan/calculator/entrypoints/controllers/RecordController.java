package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.response.RecordResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/v1/records")
public interface RecordController {

    @GetMapping
    ResponseEntity<List<RecordResponse>> getUserRecords();

    @DeleteMapping("/{id}")
    ResponseEntity<Void> softDeleteRecord(@PathVariable UUID id);
}
