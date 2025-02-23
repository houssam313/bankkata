package com.bank.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor

public class BankAccount {
    private Long id;
    private BigDecimal balance;
    private BigDecimal  overdraftLimit;
    private List<Transaction> transactions;

    public BankAccount() {
        this.balance = BigDecimal.ZERO;
        this.overdraftLimit = BigDecimal.ZERO;
        this.transactions = new ArrayList<>();
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            this.balance = this.balance.add(amount);
            this.transactions.add(new Transaction("Deposit", amount, LocalDate.now()));
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal newBalance = this.balance.subtract(amount);
            if (newBalance.compareTo(this.overdraftLimit.negate()) >= 0) {
                this.balance = newBalance;
                this.transactions.add(new Transaction("Withdrawal", amount.negate(), LocalDate.now()));
            } else {
                throw new IllegalArgumentException("Withdrawal exceeds overdraft limit");
            }
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
    }

    public String printStatement() {
        StringBuilder statement = new StringBuilder("Date       || Type       || Amount || Balance\n");
        BigDecimal bal = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            bal = bal.add(transaction.getAmount());
            String amountWithSign = transaction.getAmount().compareTo(BigDecimal.ZERO) >= 0 ? "+" + transaction.getAmount() : transaction.getAmount().toString();
            statement.append(transaction.getDate()).append(" || ")
                    .append(transaction.getType()).append(" || ")
                    .append(amountWithSign).append(" || ")
                    .append(bal).append("\n");
        }
        return statement.toString();
    }

    public String printMonthlyStatement() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        StringBuilder statement = new StringBuilder("Monthly Statement\n");
        statement.append("Type of Account: ").append(this instanceof SavingsAccount ? "Savings Account" : "Current Account").append("\n");
        statement.append("Balance as of ").append(LocalDate.now()).append(": ").append(this.getBalance()).append("\n");
        statement.append("Transactions:\n");
        statement.append("Date       || Type       || Amount || Balance\n");

        BigDecimal bal = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (transaction.getDate().isAfter(oneMonthAgo)) {
                bal = bal.add(transaction.getAmount());
                statement.append(transaction.getDate()).append(" || ")
                        .append(transaction.getType()).append(" || ")
                        .append(transaction.getAmount()).append(" || ")
                        .append(bal).append("\n");
            }
        }
        return statement.toString();
    }
}