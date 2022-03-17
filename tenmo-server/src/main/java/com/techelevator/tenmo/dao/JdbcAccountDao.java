package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(Long id) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account;
    }

    @Override
    public BigDecimal getAccountBalanceById(Long id) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()) {
            account = mapRowToAccount(rowSet);
        }
        return account.getBalance();
    }

    @Override
    public void updateAccountBalanceByIdAndBalance(Account account, Long userId, String username) {
        String sql = "UPDATE account SET balance = balance + ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, account.getBalance(), userId);

        sql = "UPDATE account SET balance = balance - ? WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?);";
        jdbcTemplate.update(sql, account.getBalance(), username);
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setUserId(rowSet.getLong("user_id"));
        account.setAccountId(rowSet.getLong("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
