package com.bank.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void testWithdrawWithinOverdraftLimit() {
        BankAccount account = new BankAccount();
        account.setOverdraftLimit(new BigDecimal("100.00")); // Set overdraft limit to 100
        account.deposit(new BigDecimal("50.00")); // Deposit 50
        account.withdraw(new BigDecimal("100.00")); // Withdraw 100 (balance = -50, within overdraft limit)
        assertEquals(new BigDecimal("-50.00"), account.getBalance());
    }

    @Test
    void testWithdrawExceedsOverdraftLimit() {
        BankAccount account = new BankAccount();
        account.setOverdraftLimit(new BigDecimal("100.00")); // Set overdraft limit to 100
        account.deposit(new BigDecimal("50.00")); // Deposit 50
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("200.00"))); // Withdraw 200 (exceeds overdraft limit)
    }

    @Test
    void testWithdrawWithoutOverdraft() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("50.00")); // Deposit 50
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(new BigDecimal("100.00"))); // Withdraw 100 (no overdraft allowed)
    }

    @Test
    void testDepositPositiveAmount() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), account.getBalance());
    }

    @Test
    void testDepositNegativeAmount() {
        BankAccount account = new BankAccount();
        assertThrows(IllegalArgumentException.class, () -> account.deposit(new BigDecimal("-50.00")));
    }

    @Test
    void testSetOverdraftLimit() {
        BankAccount account = new BankAccount();
        account.setOverdraftLimit(new BigDecimal("200.00"));
        assertEquals(new BigDecimal("200.00"), account.getOverdraftLimit());
    }

    @Test
    void testPrintStatement() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("100.00"));
        account.withdraw(new BigDecimal("50.00"));
        String statement = account.printStatement();
        assertTrue(statement.contains("Deposit || +100.00"));
        assertTrue(statement.contains("Withdrawal || -50.00"));
    }

    @Test
    void testPrintMonthlyStatement() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("100.00"));
        account.withdraw(new BigDecimal("50.00"));
        String monthlyStatement = account.printMonthlyStatement();
        assertTrue(monthlyStatement.contains("Monthly Statement"));
        assertTrue(monthlyStatement.contains("Deposit || 100.00"));
        assertTrue(monthlyStatement.contains("Withdrawal || -50.00"));
    }
}