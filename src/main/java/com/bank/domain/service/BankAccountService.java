package com.bank.domain.service;

import com.bank.domain.model.BankAccount;
import com.bank.domain.port.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository repository;

    public BankAccount createAccount() {
        BankAccount account = new BankAccount();
        repository.save(account);
        return account;
    }

    public void deposit(Long accountId, BigDecimal amount) {
        BankAccount account = getAccount(accountId);
        account.deposit(amount);
        repository.save(account);
    }

    public void withdraw(Long accountId, BigDecimal amount) {
        BankAccount account = getAccount(accountId);
        account.withdraw(amount);
        repository.save(account);
    }

    public String getStatement(Long accountId) {
        BankAccount account = getAccount(accountId);
        return account.printStatement();
    }

    public String getMonthlyStatement(Long accountId) {
        BankAccount account = getAccount(accountId);
        return account.printMonthlyStatement();
    }

    private BankAccount getAccount(Long accountId) {
        Optional<BankAccount> account = repository.findById(accountId);
        return account.orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    public void setOverdraftLimit(Long accountId, BigDecimal overdraftLimit) {
        BankAccount account = getAccount(accountId);
        account.setOverdraftLimit(overdraftLimit);
        repository.save(account);
    }

    public BigDecimal getOverdraftLimit(Long accountId) {
        BankAccount account = getAccount(accountId);
        return account.getOverdraftLimit();
    }
}