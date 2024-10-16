package com.bank.controller;

import com.bank.domain.BankAccount;
import com.bank.service.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/create") //http://localhost:8080/api/bank-accounts/create?initialBalance=1000&overdraftLimit=500
    public BankAccount createBankAccount(@RequestParam BigDecimal initialBalance,
                                         @RequestParam BigDecimal overdraftLimit) {
        return bankAccountService.createAccount(initialBalance, overdraftLimit);
    }

    @GetMapping("/account")
    public BankAccount getBankAccount(@RequestParam String id) {
        return bankAccountService.getAccount(id);
    }

    @PostMapping("/{accountId}/deposit")
    public void deposit(@PathVariable String accountId, @RequestParam BigDecimal amount) {
        bankAccountService.deposit(accountId, amount);
    }

    @PostMapping("/{accountId}/withdraw") //http://localhost:8080/api/bank-accounts/bf9e05a6-0376-4456-8926-2d6e1fe72491/withdraw?amount=100
    public void withdraw(@PathVariable String accountId, @RequestParam BigDecimal amount) {
        bankAccountService.withdraw(accountId, amount);
    }
}
