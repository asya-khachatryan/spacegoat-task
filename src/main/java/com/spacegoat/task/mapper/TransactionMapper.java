package com.spacegoat.task.mapper;

import com.spacegoat.task.domain.Transaction;
import com.spacegoat.task.dto.TransactionResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionResponseDto transactionToResponseDto(Transaction transaction);

    List<TransactionResponseDto> transactionListToResponseDtoList(List<Transaction> transaction);
}
