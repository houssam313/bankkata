package com.bank.repository;

import com.bank.domain.BankAccount;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BankAccountRepository  {

    private final Map<String, BankAccount> database = new HashMap<>();

    public BankAccount save(BankAccount account) {
        database.put(account.getAccountId(), account);
        return account;
    }

    public BankAccount findById(String accountId) {
        return database.get(accountId);
    }
}
