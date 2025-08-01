package com.fontolan.calculator.integration.stepdefs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fontolan.calculator.entrypoints.request.LoginRequest;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration
public class LoginSteps {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ResultActions response;
    private String username;
    private String password;

    @Given("a user with username {string} and password {string}")
    public void a_user_with_credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @When("the client POSTs to {string}")
    public void the_client_posts_to(String endpoint) throws Exception {
        LoginRequest request = new LoginRequest(username, password);
        response = mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    }

    @Then("the response status should be {int}")
    public void the_response_status_should_be(int status) throws Exception {
        response.andExpect(status().is(status));
    }

    @Then("the response should contain a token")
    public void the_response_should_contain_a_token() throws Exception {
        response.andExpect(jsonPath("$.token").exists());
    }
}
