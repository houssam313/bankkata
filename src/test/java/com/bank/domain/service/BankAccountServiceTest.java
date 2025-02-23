package com.bank.domain.service;

import com.bank.domain.model.BankAccount;
import com.bank.domain.port.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private BankAccountService service;

    @Test
    public void testCreateAccount() {
        BankAccount account = new BankAccount();
        when(repository.save(any(BankAccount.class))).thenReturn(account);

        BankAccount createdAccount = service.createAccount();
        assertNotNull(createdAccount);
        verify(repository, times(1)).save(any(BankAccount.class));
    }

    @Test
    public void testDeposit() {
        BankAccount account = new BankAccount();
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        service.deposit(1L, new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), account.getBalance());
        verify(repository, times(1)).save(account);
    }

    @Test
    public void testWithdraw() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("200.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        service.withdraw(1L, new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), account.getBalance());
        verify(repository, times(1)).save(account);
    }

    @Test
    public void testGetStatement() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("100.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        String statement = service.getStatement(1L);
        assertTrue(statement.contains("Deposit || +100.00"));
    }

    @Test
    public void testGetMonthlyStatement() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("100.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        String statement = service.getMonthlyStatement(1L);
        assertTrue(statement.contains("Monthly Statement"));
    }

    @Test
    public void testAccountNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.deposit(1L, new BigDecimal("100.00")));
    }

    @Test
    public void testSetOverdraftLimit() {
        BankAccount account = new BankAccount();
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        service.setOverdraftLimit(1L, new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), account.getOverdraftLimit());
        verify(repository, times(1)).save(account);
    }

    @Test
    public void testGetOverdraftLimit() {
        BankAccount account = new BankAccount();
        account.setOverdraftLimit(new BigDecimal("100.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        BigDecimal overdraftLimit = service.getOverdraftLimit(1L);
        assertEquals(new BigDecimal("100.00"), overdraftLimit);
    }

    @Test
    public void testDepositNegativeAmount() {
        BankAccount account = new BankAccount();
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> service.deposit(1L, new BigDecimal("-50.00")));
    }

    @Test
    public void testWithdrawNegativeAmount() {
        BankAccount account = new BankAccount();
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> service.withdraw(1L, new BigDecimal("-50.00")));
    }

    @Test
    public void testWithdrawExceedsBalance() {
        BankAccount account = new BankAccount();
        account.deposit(new BigDecimal("100.00"));
        when(repository.findById(1L)).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> service.withdraw(1L, new BigDecimal("200.00")));
    }
}