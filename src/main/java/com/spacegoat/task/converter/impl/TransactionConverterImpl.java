package com.spacegoat.task.converter.impl;

import com.spacegoat.task.converter.TransactionConverter;
import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.dto.TransactionResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionConverterImpl implements TransactionConverter {
    @Override
    public TransactionResponseDto convertEntityToResponseDto(Transaction transaction) {
        TransactionResponseDto dto = new TransactionResponseDto();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setSenderUserId(transaction.getSenderUserId());
        dto.setReceiverUserId(transaction.getReceiverUserId());
        dto.setTimestamp(transaction.getTimestamp());
        return dto;
    }

    @Override
    public List<TransactionResponseDto> bulkConvertEntityToResponseDto(List<Transaction> transactions) {
        return  transactions.stream().map(this::convertEntityToResponseDto).collect(Collectors.toList());
    }


}
