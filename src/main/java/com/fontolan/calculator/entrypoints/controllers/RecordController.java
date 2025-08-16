package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.domain.exception.NotFoundException;
import com.fontolan.calculator.entrypoints.controllers.docs.RecordConstants;
import com.fontolan.calculator.entrypoints.controllers.docs.StandardErrorsSecured;
import com.fontolan.calculator.entrypoints.request.RecordFilterRequest;
import com.fontolan.calculator.entrypoints.response.RecordResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Records", description = "User operation records")
public interface RecordController {
    @Operation(
            summary = "List user records",
            description = "Returns a paginated list of operation records for the authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Page.class),
                                    examples = @ExampleObject(name = "records-page", value = RecordConstants.RECORDS_PAGE_OK)
                            )
                    )
            }
    )
    @StandardErrorsSecured
    @GetMapping
    ResponseEntity<Page<RecordResponse>> getUserRecords(@AuthenticationPrincipal String username,
                                                        @Valid RecordFilterRequest request
    );

    @Operation(
            summary = "Soft delete a record",
            description = "Marks a record as deleted for the authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Record not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NotFoundException.class),
                                    examples = @ExampleObject(name = "record-not-found", value = RecordConstants.RECORD_NOT_FOUND)
                            )
                    )
            }
    )
    @StandardErrorsSecured
    @DeleteMapping("/{id}")
    ResponseEntity<Void> softDeleteRecord(@AuthenticationPrincipal String username,
                                          @PathVariable UUID id
    );
}
