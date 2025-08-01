Feature: Login

  Scenario: Successful login with valid credentials
    Given a user with username "admin@example.com" and password "admin123"
    When the client POSTs to "/api/v1/auth/login"
    Then the response status should be 200
    And the response should contain a token