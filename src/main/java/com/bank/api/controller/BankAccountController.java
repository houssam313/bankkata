package com.bank.api.controller;

import com.bank.domain.exception.AccountNotFoundException;
import com.bank.domain.exception.InvalidAmountException;
import com.bank.domain.service.BankAccountService;
import com.bank.api.dto.TransactionRequest;
import com.bank.api.dto.OverdraftLimitRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Bank Account", description = "API for managing bank accounts")
public class BankAccountController {
    private final BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @PatchMapping("/{accountId}/deposit")
    @Operation(summary = "Deposit funds", description = "Add funds to an account")
    @ApiResponse(responseCode = "200", description = "Deposit successful")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<String> deposit(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {
        try {
            BigDecimal amount = request.getAmount();

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Deposit amount must be greater than zero.");
            }

            service.deposit(accountId, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (AccountNotFoundException | InvalidAmountException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{accountId}/withdraw")
    @Operation(summary = "Withdraw funds", description = "Remove funds from an account")
    @ApiResponse(responseCode = "200", description = "Withdrawal successful")
    @ApiResponse(responseCode = "400", description = "Invalid input or insufficient funds")
    public ResponseEntity<String> withdraw(
            @PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {
        try {
            BigDecimal amount = request.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Withdrawal amount must be greater than zero.");
            }
            service.withdraw(accountId, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (AccountNotFoundException | InvalidAmountException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{accountId}/statement")
    @Operation(summary = "Get account statement", description = "Retrieve transaction history")
    @ApiResponse(responseCode = "200", description = "Statement retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid account ID")
    public ResponseEntity<String> getStatement(@PathVariable Long accountId) {
        return ResponseEntity.ok(service.getStatement(accountId));
    }

    @GetMapping("/{accountId}/monthly-statement")
    @Operation(summary = "Get monthly statement", description = "Retrieve last month's transaction history")
    @ApiResponse(responseCode = "200", description = "Monthly statement retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid account ID")
    public ResponseEntity<String> getMonthlyStatement(@PathVariable Long accountId) {
        return ResponseEntity.ok(service.getMonthlyStatement(accountId));
    }

    @PatchMapping("/{accountId}/overdraft-limit")
    @Operation(summary = "Set overdraft limit", description = "Adjust overdraft limit for an account")
    @ApiResponse(responseCode = "200", description = "Overdraft limit set successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input or account not found")
    public ResponseEntity<String> setOverdraftLimit(
            @PathVariable Long accountId,
            @Valid @RequestBody OverdraftLimitRequest request) {
        service.setOverdraftLimit(accountId, request.getOverdraftLimit());
        return ResponseEntity.ok("Overdraft limit set successfully");
    }

    @GetMapping("/{accountId}/overdraft-limit")
    @Operation(summary = "Get overdraft limit", description = "Retrieve overdraft limit")
    @ApiResponse(responseCode = "200", description = "Overdraft limit retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid account ID")
    public ResponseEntity<BigDecimal> getOverdraftLimit(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok(service.getOverdraftLimit(accountId));
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 🛠️ Global Exception Handling for Custom Errors
     */
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<String> handleAccountNotFoundException(AccountNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<String> handleInvalidAmountException(InvalidAmountException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
