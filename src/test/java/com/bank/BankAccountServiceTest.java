package com.bank;

import com.bank.domain.BankAccount;
import com.bank.repository.BankAccountRepository;
import com.bank.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount_shouldSaveAccount() {
        BigDecimal initialBalance = new BigDecimal("1000");
        BigDecimal overdraftLimit = new BigDecimal("500");
        BankAccount account = new BankAccount(initialBalance, overdraftLimit);

        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(account);

        BankAccount createdAccount = bankAccountService.createAccount(initialBalance, overdraftLimit);

        assertNotNull(createdAccount);
        assertEquals(initialBalance, createdAccount.getBalance());
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void getAccount_shouldReturnAccount() {
        String accountId = "123";
        BankAccount account = new BankAccount(new BigDecimal("1000"), new BigDecimal("500"));

        when(bankAccountRepository.findById(accountId)).thenReturn(account);

        BankAccount foundAccount = bankAccountService.getAccount(accountId);

        assertNotNull(foundAccount);
        assertEquals(new BigDecimal("1000"), foundAccount.getBalance());
        verify(bankAccountRepository, times(1)).findById(accountId);
    }

    @Test
    void deposit_shouldIncreaseBalance() {
        String accountId = "123";
        BankAccount account = new BankAccount(new BigDecimal("1000"), new BigDecimal("500"));

        when(bankAccountRepository.findById(accountId)).thenReturn(account);

        bankAccountService.deposit(accountId, new BigDecimal("200"));

        assertEquals(new BigDecimal("1200"), account.getBalance());
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        String accountId = "123";
        BankAccount account = new BankAccount(new BigDecimal("1000"), new BigDecimal("500"));

        when(bankAccountRepository.findById(accountId)).thenReturn(account);

        bankAccountService.withdraw(accountId, new BigDecimal("300"));

        assertEquals(new BigDecimal("700"), account.getBalance());
        verify(bankAccountRepository, times(1)).save(account);
    }

    @Test
    void withdraw_shouldNotAllowOverdraftWithoutPermission() {
        String accountId = "123";
        BankAccount account = new BankAccount(new BigDecimal("1000"), new BigDecimal("0"));

        when(bankAccountRepository.findById(accountId)).thenReturn(account);

        assertThrows(RuntimeException.class, () -> {
            bankAccountService.withdraw(accountId, new BigDecimal("1100"));
        });

        verify(bankAccountRepository, times(0)).save(account);
    }
}
