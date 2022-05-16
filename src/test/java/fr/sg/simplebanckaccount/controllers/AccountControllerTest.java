package fr.sg.simplebanckaccount.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
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
    public void openAccountShouldAddNewAccount() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{\"username\":\"user2\", \"password\" : \"admin\"}", headers);
        ResponseEntity<String> reponse = this.restTemplate
                .exchange(getBaseUrl() + "/openAccount", HttpMethod.POST, entity, String.class);

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reponse.getBody()).contains("Account created");
    }

    @Test
    @Order(1)
    public void testSaveMoney() throws Exception {
        ResponseEntity<String> reponse = this.restTemplate
                .withBasicAuth("admin", "admin")
                .exchange(getBaseUrl() + "/saveMoney?amount=10", HttpMethod.PUT, null, String.class);

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reponse.getBody()).contains("Your balance is now 110");
    }

    @Test
    public void testSaveMoneyUnauthorised() throws Exception {
        ResponseEntity<String> reponse = this.restTemplate
                .withBasicAuth("admin", "wrongPassword")
                .exchange(getBaseUrl() + "/saveMoney?amount=10", HttpMethod.PUT, null, String.class);

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reponse.getBody()).contains("You are not authorized to perform this action");
    }

    @Test
    @Order(2)
    public void testWithdrawoney() throws Exception {
        ResponseEntity<String> reponse = this.restTemplate
                .withBasicAuth("admin", "admin")
                .exchange(getBaseUrl() + "/withdrawMoney?amount=5", HttpMethod.PUT, null, String.class);
        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reponse.getBody()).contains("Your balance is now 105");
    }

    @Test
    public void testWithdrawoneyNegativeBalance() throws Exception {
        ResponseEntity<String> reponse = this.restTemplate
                .withBasicAuth("admin", "admin")
                .exchange(getBaseUrl() + "/withdrawMoney?amount=120", HttpMethod.PUT, null, String.class);
        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(reponse.getBody()).contains("You are not authorized to perform this action");
    }

    @Test
    @Order(3)
    public void testCheckBalance() throws Exception {
        String reponse = this.restTemplate
                .withBasicAuth("admin", "admin")
                .getForObject(getBaseUrl() + "/checkBalance", String.class);

        assertThat(reponse).contains("Your balance is 105");
        assertThat(reponse).contains("Deposit 10.0");
        assertThat(reponse).contains("Withdraw 5.0");
    }

    @Test
    public void checkBalanceUnAuthorizedUser() throws Exception {
        String reponse = this.restTemplate
                .withBasicAuth("admin", "wrongPassword")
                .getForObject(getBaseUrl() + "/checkBalance", String.class);
        assertThat(reponse).contains("You are not authorized to perform this action");
    }
}