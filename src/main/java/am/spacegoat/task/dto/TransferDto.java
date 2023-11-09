package am.spacegoat.task.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferDto {
    private Long senderId;
    private Long receiverId;
    @NotNull
    @DecimalMin(value = "1", message = "Transfer amount should be greater than or equal to 1")
    //positive
    private BigDecimal amount;
}
