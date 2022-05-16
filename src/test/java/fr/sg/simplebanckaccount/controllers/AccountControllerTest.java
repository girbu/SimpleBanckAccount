package fr.sg.simplebanckaccount.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        assertThat(this.restTemplate.getForObject(
                getBaseUrl(),
                String.class)).contains("Welcome to the Bank!");
    }

    @Test
    public void testCheckBalance() throws Exception {
        String reponse = this.restTemplate
                .withBasicAuth("admin", "admin")
                .getForObject(getBaseUrl() + "/checkBalance", String.class);

        assertThat(reponse).contains("Your balance is 100");
    }

    @Test
    public void checkBalanceUnAuthorizedUser() throws Exception {
        String reponse = this.restTemplate
                .withBasicAuth("admin", "wrongPassword")
                .getForObject(getBaseUrl() + "/checkBalance", String.class);
        assertThat(reponse).contains("You are not authorized to perform this action");
    }
}