package com.bank.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTest {

    @Test
    void testDepositWithinLimit() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        account.deposit(new BigDecimal("1000.00"));
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    void testDepositExceedsLimit() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("23000.00")));
    }

    @Test
    void testSetOverdraftLimitNotAllowed() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        assertThrows(UnsupportedOperationException.class, () -> account.setOverdraftLimit(new BigDecimal("100.00")));
    }

    @Test
    void testWithdraw() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        account.deposit(new BigDecimal("1000.00"));
        account.withdraw(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("500.00"), account.getBalance());
    }

    @Test
    void testWithdrawExceedsBalance() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        account.deposit(new BigDecimal("1000.00"));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("1500.00")));
    }

    @Test
    void testDepositNegativeAmount() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("-100.00")));
    }

    @Test
    void testWithdrawNegativeAmount() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        account.deposit(new BigDecimal("1000.00"));
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("-500.00")));
    }

    @Test
    void testDepositAtLimit() {
        SavingsAccount account = new SavingsAccount(new BigDecimal("22950.00"));
        account.deposit(new BigDecimal("22950.00"));
        assertEquals(new BigDecimal("22950.00"), account.getBalance());
    }
}