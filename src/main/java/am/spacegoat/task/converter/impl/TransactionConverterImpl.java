package am.spacegoat.task.converter.impl;

import am.spacegoat.task.converter.TransactionConverter;
import am.spacegoat.task.dto.TransactionDto;
import am.spacegoat.task.domain.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionConverterImpl implements TransactionConverter {
    @Override
    public TransactionDto convertEntityToResponseDto(Transaction transaction) {
        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setSenderUserId(transaction.getSenderUser().getId());
        dto.setReceiverUserId(transaction.getReceiverUser().getId());
        dto.setCreationTime(transaction.getCreationTime());
        return dto;
    }

    @Override
    public List<TransactionDto> bulkConvertEntityToResponseDto(List<Transaction> transactions) {
        return transactions.stream().map(this::convertEntityToResponseDto).collect(Collectors.toList());
    }


}
