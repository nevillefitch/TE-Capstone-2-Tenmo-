package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.en_old.Ac;
import org.apiguardian.api.API;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {
    private static final String API_BASE_URL = "http://localhost:8080/";
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserService(API_BASE_URL);


    public TransferService(String url) {
        this.baseUrl = url;
    }

    public void createTransfer(Long accountFrom, Long accountTo, BigDecimal balance, String token) {
        Transfer transfer = new Transfer();
        transfer.setAccountFrom(accountFrom);
        transfer.setAccountTo(accountTo);
        transfer.setAmount(balance);
        HttpEntity<Transfer> transferHttpEntity = transferHttpEntity(token, transfer);
        try {
            restTemplate.exchange(baseUrl + "transfer", HttpMethod.POST, transferHttpEntity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Something went wrong. Please try restarting");
        }
    }

    public Transfer[] getAllTransfers(String token) {
        HttpEntity<Transfer> transferHttpEntity = allTransfersHttpEntity(token);
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseUrl + "transfers", HttpMethod.GET, transferHttpEntity, Transfer[].class);
            Transfer[] transfers = response.getBody();
            return transfers;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Something went wrong. Please try restarting");
        }
        return null;
    }

    public void printListOfTransfers(Transfer[] transfers, String token, String currentUserName) {

        System.out.println("-----------------------------\nTransfers\nID      From/To       Amount\n-----------------------------");
        for (Transfer transfer : transfers) {
            try {
                UserAccount fromUserAccount = userService.getUserAccountByAccountId(transfer.getAccountFrom(), token);
                UserAccount toUserAccount = userService.getUserAccountByAccountId(transfer.getAccountTo(), token);
                if (fromUserAccount.getUsername().equals(currentUserName)) {
                    System.out.println(transfer.getTransferId() + "    To: " + toUserAccount.getUsername() + "     $" + transfer.getAmount());
                } else {
                    System.out.println(transfer.getTransferId() + "    From: " + fromUserAccount.getUsername() + "   $" + transfer.getAmount());
                }
            }catch (RestClientResponseException | ResourceAccessException e){
                BasicLogger.log(e.getMessage());
                System.out.println("Something went wrong. Please try restarting");
            }
        }
    }

    public Transfer getTransfer(Long id, String token) {
        HttpEntity<Transfer> transferHttpEntity = httpEntity(token);
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseUrl + "transfer/" + id, HttpMethod.GET, transferHttpEntity, Transfer.class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Something went wrong. Please try restarting");

        }
        return null;
    }

    public boolean transferDetails(String token) {
        System.out.println("Please enter transfer ID to view details (0 to cancel): ");
        Long transferId = scanner.nextLong();
        if (transferId.intValue() == 0) {
            return false;
        } else {
            try {
                Transfer transfer = getTransfer(transferId, token);
                if (transfer.getTransferId().equals(transferId)) {
                    UserAccount fromUserAccount = userService.getUserAccountByAccountId(transfer.getAccountFrom(), token);
                    UserAccount toUserAccount = userService.getUserAccountByAccountId(transfer.getAccountTo(), token);
                    System.out.println("----------------------------\nTransfer Details\n----------------------------");
                    System.out.println("Id:       " + transfer.getTransferId() + "\nFrom:   " + fromUserAccount.getUsername() + "\nTo:     " + toUserAccount.getUsername() +
                            "\nType:   Send" + "\nStatus: Approved" + "\nAmount: " + transfer.getAmount());
                }
            } catch (NullPointerException e) {
                BasicLogger.log(e.getMessage());
                System.out.println("Not valid transfer ID");
            }catch (RestClientResponseException | ResourceAccessException e){
                BasicLogger.log(e.getMessage());
                System.out.println("Something went wrong. Please try restarting");
            }
            return true;
        }
    }

    private HttpEntity allTransfersHttpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }


    private HttpEntity transferHttpEntity(String token, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(transfer, headers);
    }

    private HttpEntity httpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }
}
