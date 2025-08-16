package com.fontolan.calculator.entrypoints.controllers.docs;

public final class ExampleConstants {
    private ExampleConstants() {}

    public static final String DIVISION_REQ = """
        { "type": "DIVISION", "operands": [100, 5] }
    """;

    public static final String RANDOM_STRING_REQ = """
        { "type": "RANDOM_STRING", "operands": [12] }
    """;

    public static final String DIVISION_RES_OK = """
        {
            "type": "DIVISION",
            "operands": [
                100,
                5
            ],
            "result": "20",
            "userBalance": 98.65
        }
    """;

    public static final String RANDOM_STRING_RES_OK = """
        {
            "type": "RANDOM_STRING",
            "operands": [
                12
            ],
            "result": "p6IPyvpcGPJ2",
            "userBalance": 98.60
        }
    """;

    public static final String OP_TYPES_PAGE_OK = """
        {
          "content": [
            { "id": 1, "name": "ADDITION",       "cost": 1.0 },
            { "id": 2, "name": "DIVISION",       "cost": 2.0 },
            { "id": 3, "name": "SQUARE_ROOT",    "cost": 2.0 },
            { "id": 4, "name": "RANDOM_STRING",  "cost": 3.0 }
          ],
          "pageable": { "pageNumber": 0, "pageSize": 20 },
          "totalElements": 4,
          "totalPages": 1,
          "last": true,
          "size": 20,
          "number": 0,
          "sort": { "empty": true, "sorted": false, "unsorted": true },
          "first": true,
          "numberOfElements": 4,
          "empty": false
        }
    """;

    public static final String VALIDATION_ERR = """
        {
          "status": 400,
          "message": "Validation failed",
          "errors": { "length": "must be >= 1" }
        }
    """;

    public static final String UNAUTHORIZED_ERR = """
        {
          "status": 401,
          "message": "Missing or invalid token",
          "errors": { "path": "/api/v1/operations" }
        }
    """;

    public static final String FORBIDDEN_ERR = """
        {
          "status": 403,
          "message": "Access denied",
          "errors": { "path": "/api/v1/operations" }
        }
    """;

    public static final String UNEXPECTED_ERR = """
        {
          "status": 500,
          "message": "Unexpected error",
          "errors": { "path": "/api/v1/operations" }
        }
    """;
}
