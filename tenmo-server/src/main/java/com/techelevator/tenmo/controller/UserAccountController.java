package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserAccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class UserAccountController {
    private UserAccountDao dao;

    public UserAccountController(UserAccountDao dao){
        this.dao = dao;
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/allusers", method = RequestMethod.GET)
    public List<UserAccount> getAllUsers(Principal principal){
        return dao.findAllUsersExcludingCurrent(principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/useraccount/{id}", method = RequestMethod.GET)
    public UserAccount getUserAccountByAccountId(@Valid @PathVariable Long id){
        return dao.findUserByAccountId(id);
    }

}
