package com.bank.api;

import com.bank.api.controller.BankAccountController;
import com.bank.domain.service.BankAccountService;
import com.bank.domain.exception.AccountNotFoundException;
import com.bank.domain.exception.InvalidAmountException;
import com.bank.api.dto.TransactionRequest;
import com.bank.api.dto.OverdraftLimitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BankAccountControllerTest {

    @Mock
    private BankAccountService service;

    @InjectMocks
    private BankAccountController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void deposit_Success() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("100.00"));

        mockMvc.perform(patch("/api/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Deposit successful"));
    }

    @Test
    void deposit_InvalidAmount() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("-100.00"));

        mockMvc.perform(patch("/api/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Deposit amount must be greater than zero."));
    }

    @Test
    void deposit_AccountNotFound() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("100.00"));

        doThrow(new AccountNotFoundException("Account not found")).when(service).deposit(anyLong(), any(BigDecimal.class));

        mockMvc.perform(patch("/api/accounts/1/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account not found"));
    }

    @Test
    void withdraw_Success() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("50.00"));

        mockMvc.perform(patch("/api/accounts/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Withdrawal successful"));
    }

    @Test
    void withdraw_InvalidAmount() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("-50.00"));

        mockMvc.perform(patch("/api/accounts/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Withdrawal amount must be greater than zero."));
    }

    @Test
    void withdraw_AccountNotFound() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setAmount(new BigDecimal("50.00"));

        doThrow(new AccountNotFoundException("Account not found")).when(service).withdraw(anyLong(), any(BigDecimal.class));

        mockMvc.perform(patch("/api/accounts/1/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Account not found"));
    }

    @Test
    void getStatement_Success() throws Exception {
        when(service.getStatement(anyLong())).thenReturn("Account statement");

        mockMvc.perform(get("/api/accounts/1/statement"))
                .andExpect(status().isOk())
                .andExpect(content().string("Account statement"));
    }

    @Test
    void getMonthlyStatement_Success() throws Exception {
        when(service.getMonthlyStatement(anyLong())).thenReturn("Monthly statement");

        mockMvc.perform(get("/api/accounts/1/monthly-statement"))
                .andExpect(status().isOk())
                .andExpect(content().string("Monthly statement"));
    }

    @Test
    void setOverdraftLimit_Success() throws Exception {
        OverdraftLimitRequest request = new OverdraftLimitRequest();
        request.setOverdraftLimit(new BigDecimal("500.00"));

        mockMvc.perform(patch("/api/accounts/1/overdraft-limit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Overdraft limit set successfully"));
    }

    @Test
    void getOverdraftLimit_Success() throws Exception {
        when(service.getOverdraftLimit(anyLong())).thenReturn(new BigDecimal("500.00"));

        mockMvc.perform(get("/api/accounts/1/overdraft-limit"))
                .andExpect(status().isOk())
                .andExpect(content().string("500.00"));
    }

    @Test
    void handleAccountNotFoundException() throws Exception {
        when(service.getStatement(anyLong())).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/api/accounts/1/statement"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Account not found"));
    }

    @Test
    void handleInvalidAmountException() throws Exception {
        when(service.getStatement(anyLong())).thenThrow(new InvalidAmountException("Invalid amount"));

        mockMvc.perform(get("/api/accounts/1/statement"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid amount"));
    }
}