package com.techelevator.tenmo;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;
import com.techelevator.util.BasicLogger;
import io.cucumber.java.an.E;
import org.apiguardian.api.API;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);
    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
//        System.out.println(accountService.getAccount(currentUser.getUser().getId(), currentUser.getToken()).getBalance());
        System.out.println(accountService.getBalance(currentUser.getUser().getId(), currentUser.getToken()));

    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        while (true) {
            try {
                transferService.printListOfTransfers(transferService.getAllTransfers(currentUser.getToken()), currentUser.getToken(), currentUser.getUser().getUsername());
            } catch (RestClientResponseException | ResourceAccessException e) {
                BasicLogger.log(e.getMessage());
                System.out.println("Something went wrong. Please try restarting");
            }
            boolean run = transferService.transferDetails(currentUser.getToken());
            if (!run) {
                break;
            }
            consoleService.pause();
        }
    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        System.out.println("-- Users to send money to -- \n");
        int num = 1;
        try {
            while (num == 1) {
                userService.printListOfUsers(userService.getAllUsers(currentUser.getToken()));
                userService.listOfUserAccounts(userService.getAllUsers(currentUser.getToken()));
                Long userId = accountService.selectUser();
                if (userId.intValue() != 0) {
                    Long accountId = accountService.getAccount(userId, currentUser.getToken()).getAccountId();
                    Account currentUserAccount = accountService.getAccount(currentUser.getUser().getId(), currentUser.getToken());
                    Long currentUserAccountId = currentUserAccount.getAccountId();
                    BigDecimal amount = accountService.amountToSend();
                    BigDecimal compareAmount = accountService.getBalance(currentUser.getUser().getId(), currentUser.getToken());
                    if (compareAmount.compareTo(amount) == 0 || compareAmount.compareTo(amount) == 1) {
                        accountService.sendMoney(amount, userId, accountId, currentUser.getToken());
                        transferService.createTransfer(currentUserAccountId, accountId, amount, currentUser.getToken());
                    } else {
                        System.out.printf("You do not have enough money to send\n");
                        num = 0;
                    }
                } else {
                    num = 0;
                }
            }
        } catch (NullPointerException e) {
            BasicLogger.log(e.getMessage());
            System.out.println("Incorrect input. Please try again.");
        } catch (Exception e) {
            BasicLogger.log(e.getMessage());
        }

    }

    private void requestBucks() {
        // TODO Auto-generated method stub

    }

}
