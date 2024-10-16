package com.bank.domain;

import lombok.Data;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Data
public class BankAccount {
    private String accountId;
    private BigDecimal balance;
    private BigDecimal overdraftLimit;

    public BankAccount(BigDecimal balance, BigDecimal overdraftLimit) {
        this.accountId = UUID.randomUUID().toString();
        this.balance = balance;
        this.overdraftLimit = overdraftLimit;
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (this.balance.subtract(amount).compareTo(overdraftLimit.negate()) >= 0) {
            this.balance = this.balance.subtract(amount);
        } else {
            throw new RuntimeException("Withdrawal exceeds overdraft limit");
        }
    }
}
