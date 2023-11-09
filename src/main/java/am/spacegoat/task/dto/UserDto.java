package am.spacegoat.task.dto;

import am.spacegoat.task.domain.UserEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private BigDecimal balance;

    public static UserEntity toEntity(UserDto dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(dto.getId());
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setUsername(dto.getUsername());
        userEntity.setCity(dto.getCity());
        userEntity.setBalance(dto.getBalance());
        return userEntity;
    }

    public interface UserCreation {

    }
}

