package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.UserAccount;

import java.util.List;

public interface UserAccountDao {

    List<UserAccount> findAllUsersExcludingCurrent(String username);

    UserAccount findUserByAccountId(Long accountId);
}
