package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class AccountController {
    private AccountDao dao;

    public AccountController(AccountDao dao){
        this.dao = dao;
    }

    @RequestMapping(value = "/account/{id}", method = RequestMethod.GET)
    public Account getAccount(@Valid  @PathVariable Long id){
        return dao.getAccountById(id);
    }

    @RequestMapping(value = "/accountbalance/{id}", method = RequestMethod.GET)
    public BigDecimal getBalance(@Valid @PathVariable Long id){
        return dao.getAccountBalanceById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/accounttransfer/{id}", method = RequestMethod.PUT)
    public void updateAccountBalanceByIdAndBalance(@Valid @RequestBody Account account, @PathVariable Long id, Principal principal){
        dao.updateAccountBalanceByIdAndBalance(account, id, principal.getName());
    }

}
