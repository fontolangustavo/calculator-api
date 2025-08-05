package com.fontolan.calculator.entrypoints.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OperationFilterRequest {

    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 10;

    @Pattern(regexp = "asc|desc", flags = Pattern.Flag.CASE_INSENSITIVE)
    private String sortDir = "desc";

    private String sortBy = "type";
}