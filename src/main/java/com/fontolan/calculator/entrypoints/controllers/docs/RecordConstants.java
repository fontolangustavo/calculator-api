package com.fontolan.calculator.entrypoints.controllers.docs;

public class RecordConstants {
    private RecordConstants() {}

    public static final String RECORDS_PAGE_OK = """
        {
            "content": [
                {
                    "id": "bcc949ec-a0ae-4d5b-beee-141353efc34f",
                    "operationType": "RANDOM_STRING",
                    "amount": "[12]",
                    "operationResponse": "ZcgT8YJKrtvS",
                    "userBalance": 98.45,
                    "createdAt": "2025-08-16T15:51:22.01573"
                },
                {
                    "id": "7812d03b-90fd-426c-a57c-6132e0d528bb",
                    "operationType": "RANDOM_STRING",
                    "amount": "[12]",
                    "operationResponse": "6USxU4h2CIwT",
                    "userBalance": 98.50,
                    "createdAt": "2025-08-16T14:58:46.399515"
                },
                {
                    "id": "3640b63d-2fd4-49ea-a006-17c4625ba858",
                    "operationType": "RANDOM_STRING",
                    "amount": "[12]",
                    "operationResponse": "GTQHpQbtBj2z",
                    "userBalance": 98.55,
                    "createdAt": "2025-08-16T14:58:36.067601"
                }
            ],
            "pageable": {
                "pageNumber": 0,
                "pageSize": 10,
                "sort": [
                    {
                        "direction": "DESC",
                        "property": "createdAt",
                        "ignoreCase": false,
                        "nullHandling": "NATIVE",
                        "descending": true,
                        "ascending": false
                    }
                ],
                "offset": 0,
                "paged": true,
                "unpaged": false
            },
            "last": false,
            "totalElements": 41,
            "totalPages": 5,
            "size": 10,
            "number": 0,
            "sort": [
                {
                    "direction": "DESC",
                    "property": "createdAt",
                    "ignoreCase": false,
                    "nullHandling": "NATIVE",
                    "descending": true,
                    "ascending": false
                }
            ],
            "first": true,
            "numberOfElements": 10,
            "empty": false
        }
    """;

    public static final String RECORD_NOT_FOUND = """
        {
            "status": 404,
            "message": "Record not found: bcc949ec-a0ae-4d5b-beee-0141353efc34",
            "errors": {
                "path": "/api/v1/records/bcc949ec-a0ae-4d5b-beee-141353efc34"
            }
        }
    """;
}
