package am.spacegoat.task.dto;

import am.spacegoat.task.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(groups = UserCreation.class, message = "First name cannot be blank")
    private String firstName;

    @NotBlank(groups = UserCreation.class, message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(groups = UserCreation.class, message = "Username cannot be blank during user creation")
    private String username;

    @NotBlank(groups = UserCreation.class, message = "City cannot be blank")
    private String city;

    @NotNull(groups = UserCreation.class, message = "Balance cannot be null")
    private BigDecimal balance;

    public static User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setCity(dto.getCity());
        user.setBalance(dto.getBalance());
        return user;
    }

    public interface UserCreation {

    }
}

