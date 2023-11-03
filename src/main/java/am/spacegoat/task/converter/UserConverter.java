package am.spacegoat.task.converter;

import am.spacegoat.task.domain.User;
import am.spacegoat.task.dto.UserDto;

public interface UserConverter {
    UserDto convertEntityToResponseDto(User user);

    User convertRequestDtoToEntity(UserDto dto);

}
