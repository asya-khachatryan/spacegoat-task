package am.spacegoat.task.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundsDto {
    private Long userId;
    @NotNull(groups = {AddFunds.class, ReduceFunds.class})
    @DecimalMin(value = "1", groups = AddFunds.class,
            message = "Amount for funds addition should be less than or equal to 1")
    @DecimalMax(value = "-1", groups = ReduceFunds.class,
            message = "Amount for funds reduction should be less than or equal to -1")
    @Positive
    @Negative
    //interface
    private BigDecimal amount;

    public interface AddFunds {

    }

    public interface ReduceFunds {

    }
}
