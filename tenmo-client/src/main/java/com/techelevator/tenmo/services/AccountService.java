package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.UserAccount;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private Scanner scanner = new Scanner(System.in);
    public AccountService(String url){
        this.baseUrl = url;
    }

    public Account getAccount(Long id, String token){
        HttpEntity<Account> accountHttpEntity = httpEntity(token);
       try {
           ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "account/" + id, HttpMethod.GET, accountHttpEntity, Account.class);
           return response.getBody();
       }catch (RestClientResponseException | ResourceAccessException e){
           BasicLogger.log(e.getMessage());
           System.out.println("Something went wrong. Please try restarting");
       }
       return null;
    }
    public BigDecimal getBalance(Long id, String token){
        HttpEntity<Account> accountHttpEntity = httpEntity(token);
        try{
            ResponseEntity<BigDecimal> response = restTemplate.exchange(baseUrl + "accountbalance/" + id, HttpMethod.GET, accountHttpEntity, BigDecimal.class);
            return response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return null;
    }
    public BigDecimal amountToSend() {
        boolean isPositive = false;
        BigDecimal amount = BigDecimal.ZERO;
        while (!isPositive) {
            System.out.println("Select amount to send:");
            amount = scanner.nextBigDecimal();
            if (amount.compareTo(BigDecimal.ZERO) == 0 || amount.compareTo(BigDecimal.ZERO) == -1) {
                System.out.println("Amount to send cannot be equal to or less than 0\nPlease try again.\n");
            } else {
                isPositive = true;
            }
        }
        return amount;
    }
    public Long selectUser(){
        Long id = scanner.nextLong();
        return id;
    }
    public void sendMoney(BigDecimal balance, Long userId, Long accountId, String token) {
        Account account = new Account();
        account.setBalance(balance);
        account.setUserId(userId);
        account.setAccountId(accountId);
        HttpEntity<Account> accountHttpEntity = sendMoneyHttpEntity(token, account);
        try {
            restTemplate.exchange(baseUrl + "/accounttransfer/" + userId, HttpMethod.PUT, accountHttpEntity, Account.class);
            System.out.println("Send request successful\n");
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Something went wrong. Please try restarting");
        }
    }
    private HttpEntity sendMoneyHttpEntity(String token, Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity httpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
