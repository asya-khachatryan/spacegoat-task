package am.spacegoat.task.mapper;

import am.spacegoat.task.domain.Transaction;
import am.spacegoat.task.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDto transactionToResponseDto(Transaction transaction);

    List<TransactionDto> transactionListToResponseDtoList(List<Transaction> transaction);
}
