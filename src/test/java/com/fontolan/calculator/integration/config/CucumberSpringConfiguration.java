package com.fontolan.calculator.integration.config;


import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "security.jwt.secret=this-is-my-fake-test-secret-for-use-in-test",
        "security.jwt.expiration=3600000",
        "random-org.api-key=fake-random-api-key"
})
public class CucumberSpringConfiguration { }
