package am.spacegoat.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;
    private Long senderUserId;
    private Long receiverUserId;
    private BigDecimal amount;
    private LocalDateTime creationTime;
}
