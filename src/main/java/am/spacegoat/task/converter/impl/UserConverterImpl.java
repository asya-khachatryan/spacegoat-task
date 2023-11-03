package am.spacegoat.task.converter.impl;

import am.spacegoat.task.converter.UserConverter;
import am.spacegoat.task.domain.User;
import am.spacegoat.task.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserDto convertEntityToResponseDto(User user) {
        UserDto responseDto = new UserDto();
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setUsername(user.getUsername());
        responseDto.setCity(user.getCity());
        responseDto.setBalance(user.getBalance());
        responseDto.setId(user.getId());
        return responseDto;
    }

    @Override
    public User convertRequestDtoToEntity(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setCity(dto.getCity());
        user.setBalance(dto.getBalance());
        return user;
    }
}
