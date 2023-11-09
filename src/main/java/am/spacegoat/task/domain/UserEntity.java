package am.spacegoat.task.domain;


import am.spacegoat.task.dto.UserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    public static UserDto toDto(UserEntity userEntity) {
        UserDto dto = new UserDto();
        dto.setFirstName(userEntity.getFirstName());
        dto.setLastName(userEntity.getLastName());
        dto.setUsername(userEntity.getUsername());
        dto.setCity(userEntity.getCity());
        dto.setBalance(userEntity.getBalance());
        dto.setId(userEntity.getId());
        return dto;
    }
}
