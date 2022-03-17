package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class UserService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();
    private final Scanner scanner = new Scanner(System.in);
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserService(String url) {
        this.baseUrl = url;
    }

    public UserAccount[] getAllUsers(String token) {
        HttpEntity<UserAccount> userHttpEntity = httpEntity(token);
        try {
            ResponseEntity<UserAccount[]> response = restTemplate.exchange(baseUrl + "allusers", HttpMethod.GET, userHttpEntity, UserAccount[].class);
            UserAccount[] users = response.getBody();
            return users;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Something went wrong. Please try restarting");
        }
        return null;
    }

    public void printListOfUsers(UserAccount[] users) {
        System.out.println("Users\n" + "ID     " + "Name\n-----------------");
        for (UserAccount user : users) {
            System.out.println(user.getId() + "   " + user.getUsername());
        }
        System.out.println("\nPlease choose a provided user ID to send money. (0 to cancel):");
    }

    public UserAccount getUserAccountByAccountId(Long id,String token){
        HttpEntity<UserAccount> userAccountHttpEntity = httpEntity(token);
        try{
            ResponseEntity<UserAccount> response = restTemplate.exchange(baseUrl + "useraccount/" + id, HttpMethod.GET, userAccountHttpEntity, UserAccount.class);
            return response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Something went wrong. Please try restarting");
        }
        return null;
    }
    public List<UserAccount> listOfUserAccounts(UserAccount[] userAccountArray){
        List<UserAccount> userAccountList = new ArrayList<>();
        for (UserAccount userAccount : userAccountArray){
            userAccountList.add(userAccount);
        }
        return userAccountList;
    }

    private HttpEntity httpEntity(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

}
