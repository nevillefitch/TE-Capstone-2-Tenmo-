package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
        List<Transfer> listOfTransfers();

        Transfer getTransferByTransferId(Long id);

        List<Transfer> getAllTransfers(String username);

        Transfer createSendTransfer(Long accountFrom, Long accountTo, BigDecimal amount);
}
