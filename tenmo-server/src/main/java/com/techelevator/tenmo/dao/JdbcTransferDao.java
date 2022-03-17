package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Transfer> listOfTransfers() {

        return null;
    }

    @Override
    public Transfer getTransferByTransferId(Long id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                     "FROM transfer WHERE transfer_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()){
            transfer = mapRowToTransfer(rowSet);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getAllTransfers(String username) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer " +
                     "WHERE account_from = (SELECT account_id FROM account WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?))" +
                     "OR account_to = (SELECT account_id FROM account WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?));";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username, username);
        while(rowSet.next()){
            Transfer transfer = mapRowToTransfer(rowSet);
            transfers.add(transfer);
        }
//        sql = "SELECT * FROM transfer " +
//                "WHERE account_to = (SELECT account_id FROM account WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ?))";
//        SqlRowSet rowSet2 = jdbcTemplate.queryForRowSet(sql, username);
//        while(rowSet2.next()){
//
//        }
        return transfers;
    }
//    public List<Transfer> getAllTransfersSentToUser(String username) {
//
//    }


    @Override
    public Transfer createSendTransfer(Long accountFrom, Long accountTo, BigDecimal amount) {
        Transfer transfer = null;
        String sql = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                     "VALUES ((SELECT transfer_type_id FROM transfer_type WHERE transfer_type_desc = 'Send' LIMIT 1), " +
                     "(SELECT transfer_status_id FROM transfer_status WHERE transfer_status_desc = 'Approved' LIMIT 1),?,?,?) RETURNING transfer_id;";
        try{
            Long transferId = jdbcTemplate.queryForObject(sql, Long.class, accountFrom, accountTo, amount);
            transfer = getTransferByTransferId(transferId);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rs.getLong("transfer_id"));
        transfer.setTransferType(rs.getLong("transfer_type_id"));
        transfer.setTransferStatus(rs.getLong("transfer_status_id"));
        transfer.setAccountFrom(rs.getLong("account_from"));
        transfer.setAccountTo(rs.getLong("account_to"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        return transfer;
    }
}
