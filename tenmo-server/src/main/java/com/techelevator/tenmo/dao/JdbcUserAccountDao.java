package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcUserAccountDao implements UserAccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcUserAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserAccount> findAllUsersExcludingCurrent(String username) {
        List<UserAccount> users = new ArrayList<>();
        String sql = "SELECT user_id, username FROM tenmo_user WHERE username != ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        while(results.next()) {
            UserAccount user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }
    @Override
    public UserAccount findUserByAccountId(Long accountId){
        UserAccount userAccount = null;
        String sql = "SELECT user_id, username FROM tenmo_user WHERE user_id = (SELECT user_id FROM account WHERE account_id = ?);";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()){
            userAccount = mapRowToUser(results);
        }
        return userAccount;
    }
    private UserAccount mapRowToUser(SqlRowSet rs) {
        UserAccount user = new UserAccount();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        return user;
    }
}
