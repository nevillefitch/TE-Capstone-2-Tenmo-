package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountById(Long id);

    BigDecimal getAccountBalanceById(Long id);

    void updateAccountBalanceByIdAndBalance(Account account, Long userId, String username);


}
