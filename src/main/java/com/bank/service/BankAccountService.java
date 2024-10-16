package com.bank.service;


import com.bank.domain.BankAccount;
import com.bank.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount createAccount(BigDecimal initialBalance, BigDecimal overdraftLimit) {
        BankAccount account = new BankAccount(initialBalance, overdraftLimit);
        return bankAccountRepository.save(account);
    }

    public BankAccount getAccount(String id) {
        return bankAccountRepository.findById(id);
    }

    public void deposit(String accountId, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(accountId);
        account.deposit(amount);
        bankAccountRepository.save(account);
    }

    public void withdraw(String accountId, BigDecimal amount) {
        BankAccount account = bankAccountRepository.findById(accountId);
        account.withdraw(amount);
        bankAccountRepository.save(account);
    }
}
