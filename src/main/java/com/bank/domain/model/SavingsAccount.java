package com.bank.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class SavingsAccount extends BankAccount {
    private final BigDecimal depositLimit; // Deposit limit for savings account

    public SavingsAccount(BigDecimal depositLimit) {
        super();
        this.depositLimit = depositLimit;
    }

    @Override
    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        throw new UnsupportedOperationException("Savings accounts cannot have an overdraft limit");
    }

    @Override
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newBalance = this.getBalance().add(amount);
            if (newBalance.compareTo(depositLimit) <= 0) {
                super.deposit(amount);
            } else {
                throw new IllegalArgumentException("Deposit exceeds the account limit");
            }
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            if (this.getBalance().compareTo(amount) >= 0) {
                super.withdraw(amount);
            } else {
                throw new IllegalArgumentException("Insufficient funds");
            }
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
    }
}