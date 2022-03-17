package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {
    Long transferId;
    Long transferType;
    Long transferStatus;
    Long accountFrom;
    Long accountTo;
    BigDecimal amount;

    public Transfer() {
    }

    public Transfer(Long transferId, Long transferType, Long transferStatus, Long accountFrom, Long accountTo, BigDecimal amount) {
        this.transferId = transferId;
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Long getTransferType() {
        return transferType;
    }

    public void setTransferType(Long transferType) {
        this.transferType = transferType;
    }

    public Long getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Long transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Long getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Long getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Long accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", transferType='" + transferType +
                ", transferStatus=" + transferStatus +
                ", accountFrom=" + accountFrom +
                ", accountTo=" + accountTo +
                ", amount=" + amount +
                "}";
    }
}
//    Long transferId;
//    Long transferType;
//    Long transferStatus;
//    Long accountFrom;
//    Long accountTo;
//    BigDecimal amount;
