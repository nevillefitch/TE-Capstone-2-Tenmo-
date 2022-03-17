package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    private Long userId;
    private Long accountId;
    private BigDecimal balance;

    public Account() {
    }

    public Account(Long userId, Long accountId, BigDecimal balance) {
        this.userId = userId;
        this.accountId = accountId;
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "userId=" + userId +
                "accountId=" + accountId +
                "balance=" + balance +
                "}";
    }

//    public String toString() {
//        return "LoginDTO{" +
//                "username='" + username + '\'' +
//                ", password='" + password + '\'' +
//                '}';
//    }
}
