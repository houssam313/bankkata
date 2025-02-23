package com.bank.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class OverdraftLimitRequest {
    private BigDecimal overdraftLimit;
}