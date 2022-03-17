package com.techelevator.tenmo.model;

import java.time.LocalDate;

public class UserAccount {

    private Long id;
    private String username;

    public UserAccount(){}

    public UserAccount(Long id, String username){
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
