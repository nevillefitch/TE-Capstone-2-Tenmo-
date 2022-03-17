package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class TransferController {
    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao){
        this.accountDao = accountDao;
        this.transferDao = transferDao;
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer){
        return transferDao.createSendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal){
        return transferDao.getAllTransfers(principal.getName());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferByTransferId (@Valid @PathVariable Long id){
        return transferDao.getTransferByTransferId(id);
    }
}
