package com.fontolan.calculator.entrypoints.controllers;

import com.fontolan.calculator.entrypoints.controllers.docs.ExampleConstants;
import com.fontolan.calculator.entrypoints.controllers.docs.StandardErrorsSecured;
import com.fontolan.calculator.entrypoints.request.OperationFilterRequest;
import com.fontolan.calculator.entrypoints.request.OperationRequest;
import com.fontolan.calculator.entrypoints.response.OperationResponse;
import com.fontolan.calculator.entrypoints.response.OperationTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operations")
@Tag(name = "Operations", description = "Arithmetic operations and random string generation")
public interface OperationController {
    @Operation(
            summary = "Request a new operation",
            description = "Performs an arithmetic operation (e.g. DIVISION, SQUARE_ROOT) or a RANDOM_STRING generation.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OperationRequest.class),
                            examples = {
                                    @ExampleObject(name = "division", value = ExampleConstants.DIVISION_REQ),
                                    @ExampleObject(name = "random-string", value = ExampleConstants.RANDOM_STRING_REQ)
                            }
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = OperationResponse.class),
                    examples = {
                            @ExampleObject(name = "division", value = ExampleConstants.DIVISION_RES_OK),
                            @ExampleObject(name = "random-string", value = ExampleConstants.RANDOM_STRING_RES_OK)
                    }
            )
    )
    @StandardErrorsSecured
    @PostMapping
    ResponseEntity<OperationResponse> executeOperation(
            @Parameter(hidden = true) @AuthenticationPrincipal String username,
            @Valid @RequestBody OperationRequest request
    );

    @Operation(
            summary = "List available operation types",
            description = "Returns a pageable list of available operations. You can filter/paginate via query params."
    )
    @ApiResponse(
            responseCode = "200",
            description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Page.class),
                    examples = @ExampleObject(name = "types-page", value = ExampleConstants.OP_TYPES_PAGE_OK)
            )
    )
    @StandardErrorsSecured
    @GetMapping("/types")
    ResponseEntity<Page<OperationTypeResponse>> listAvailableOperations(
            @Valid OperationFilterRequest request
    );
}