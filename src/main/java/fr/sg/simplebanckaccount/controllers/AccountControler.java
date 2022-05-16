package fr.sg.simplebanckaccount.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.sg.simplebanckaccount.domain.Account;
import fr.sg.simplebanckaccount.domain.Transaction;

@RestController
public class AccountControler {

    private static final Logger LOG = LoggerFactory.getLogger(AccountControler.class);

    final HashMap<String, Account> accounts = new HashMap<String, Account>();

    @PostConstruct
    public void init() {
        Account account = new Account("admin", "admin", 100.0, new ArrayList<Transaction>());
        accounts.put("admin", account);
        System.out.println(accounts);
    }

    @GetMapping("/")
    public String welcome() {
        return "Welcome to the Bank!";
    }

    @PutMapping("/saveMoney")
    public String saveMoney(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam Double amount) {
        String[] credentials = getCredentials(auth);

        Account account = accounts.get(credentials[0]);
        if (account != null && account.getPassword().equals(credentials[1])) {
            account.setBalance(account.getBalance() + amount);
            account.addTransaction(new Transaction(amount, LocalDateTime.now(), "Deposit"));
            LOG.info("Deposit of {}€ done on account {}", amount, account.getUser());
            return "Your balance is now " + account.getBalance();
        } else {
            LOG.info("Deposit of {}€ failed on account {}", amount, credentials[0]);
            return "You are not authorized to perform this action";
        }
    }

    @PutMapping("/withdrawMoney")
    public String withdrawMoney(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth, @RequestParam Double amount) {
        String[] credentials = getCredentials(auth);
        Account account = accounts.get(credentials[0]);
        if (account != null && account.getPassword().equals(credentials[1]) && account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            account.addTransaction(new Transaction(amount, LocalDateTime.now(), "Withdraw"));
            LOG.info("Withdraw of {}€ done on account {}", amount, account.getUser());
            return "Your balance is now " + account.getBalance();
        } else {
            LOG.info("Withdraw of {}€ failed on account {}", amount, credentials[0]);
            return "You are not authorized to perform this action";
        }
    }

    private String[] getCredentials(String encodedAuth) {
        String[] authParts = encodedAuth.split(" ");
        String authInfo = authParts[1];
        String decodedAuth = new String(Base64.getDecoder().decode(authInfo));
        return decodedAuth.split(":");
    }
}
