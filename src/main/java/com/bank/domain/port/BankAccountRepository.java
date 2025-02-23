package com.bank.domain.port;

import com.bank.domain.model.BankAccount;

import java.util.Optional;

public interface BankAccountRepository {
    Optional<BankAccount> findById(Long id);
    BankAccount  save(BankAccount account);
}