package com.bank.infrastructure.adapter;



import com.bank.domain.model.BankAccount;
import com.bank.domain.port.BankAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryBankAccountRepository implements BankAccountRepository {
    private final Map<Long, BankAccount> accounts = new HashMap<>();
    private Long nextId = 1L;

    @Override
    public Optional<BankAccount> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    @Override
    public BankAccount  save(BankAccount account) {
        if (account.getId() == null) {
            account.setId(nextId++);
        }
        accounts.put(account.getId(), account);
        return account;
    }
}