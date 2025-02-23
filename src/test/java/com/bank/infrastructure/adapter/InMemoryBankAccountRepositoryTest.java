package com.bank.infrastructure.adapter;

import com.bank.domain.model.BankAccount;
import com.bank.domain.port.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryBankAccountRepositoryTest {

    private BankAccountRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new InMemoryBankAccountRepository();
    }

    @Test
    void testSaveNewAccount() {
        // Arrange
        BankAccount account = new BankAccount();

        // Act
        repository.save(account);

        // Assert
        assertNotNull(account.getId()); // ID should be assigned
        Optional<BankAccount> savedAccount = repository.findById(account.getId());
        assertTrue(savedAccount.isPresent());
        assertEquals(account, savedAccount.get());
    }

    @Test
    void testSaveExistingAccount() {
        // Arrange
        BankAccount account = new BankAccount();
        repository.save(account); // Save the account once
        account.deposit(new BigDecimal("100.00")); // Modify the account

        // Act
        repository.save(account); // Save the account again

        // Assert
        Optional<BankAccount> savedAccount = repository.findById(account.getId());
        assertTrue(savedAccount.isPresent());
        assertEquals(new BigDecimal("100.00"), savedAccount.get().getBalance()); // Ensure changes are saved
    }

    @Test
    void testFindByIdExistingAccount() {
        // Arrange
        BankAccount account = new BankAccount();
        repository.save(account);

        // Act
        Optional<BankAccount> foundAccount = repository.findById(account.getId());

        // Assert
        assertTrue(foundAccount.isPresent());
        assertEquals(account, foundAccount.get());
    }

    @Test
    void testFindByIdNonExistentAccount() {
        // Arrange
        long nonExistentId = 999L;

        // Act
        Optional<BankAccount> foundAccount = repository.findById(nonExistentId);

        // Assert
        assertFalse(foundAccount.isPresent());
    }
}