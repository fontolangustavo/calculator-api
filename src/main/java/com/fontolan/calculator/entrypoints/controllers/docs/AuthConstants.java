package com.fontolan.calculator.entrypoints.controllers.docs;

public final class AuthConstants {
    private AuthConstants() {}

    public static final String LOGIN_REQ = """
        { "username": "john.doe", "password": "secret" }
    """;

    public static final String LOGIN_RES_OK = """
        { "accessToken": "eyJhbGciOi...", "tokenType": "Bearer" }
    """;

    public static final String LOGIN_RES_INVALID_CREDENTIALS = """
        {
            "status": 422,
            "message": "Invalid credentials",
            "errors": {
                "path": "/api/v1/auth/login"
            }
        }
    """;

    public static final String REGISTER_REQ = """
        { "username": "john.doe", "password": "secret" }
    """;

    public static final String REGISTER_RES_OK = """
        {
            "token": "JWT-TOKEN",
            "user": {
                "id": "be1ee7b4-bf0c-46b3-8fec-c1d00f0e2d07",
                "username": "john.doe",
                "status": "ACTIVE"
            }
        }
    """;

    public static final String REGISTER_RES_UNPROCESSABLE_ENTITY = """
        {
            "status": 422,
            "message": "Username already exists: john.doe",
            "errors": {
                "path": "/api/v1/auth/register"
            }
        }
    """;

    public static final String ME_RES_OK = """
        {
            "id": "2da3cf5c-2439-404f-a3c8-a13e96ba0dca",
            "username": "john.doe",
            "balance": 98.45,
            "status": "ACTIVE"
        }
    """;

}
