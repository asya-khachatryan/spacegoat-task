package com.spacegoat.task.converter;

import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.dto.TransactionResponseDto;

import java.util.List;

public interface TransactionConverter {
    TransactionResponseDto convertEntityToResponseDto(Transaction transaction);

    List<TransactionResponseDto> bulkConvertEntityToResponseDto(List<Transaction> transactions);

}
