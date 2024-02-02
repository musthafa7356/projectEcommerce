package com.example.library.dto;

import com.example.library.enums.WalletTransactionType;
import com.example.library.model.Wallet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletHistoryDto {

    private long id;

    private double amount;

    private WalletTransactionType type;

    private String transactionStatus;

    private Wallet wallet;

    private LocalDate transactionDate;

    private LocalTime localTime;

}
