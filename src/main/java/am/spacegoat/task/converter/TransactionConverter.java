package am.spacegoat.task.converter;

import am.spacegoat.task.domain.Transaction;
import am.spacegoat.task.dto.TransactionDto;

import java.util.List;

public interface TransactionConverter {
    TransactionDto convertEntityToResponseDto(Transaction transaction);

    List<TransactionDto> bulkConvertEntityToResponseDto(List<Transaction> transactions);

}
