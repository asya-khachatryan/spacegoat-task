package am.spacegoat.task.converter;

import am.spacegoat.task.domain.UserEntity;
import am.spacegoat.task.dto.UserDto;

public interface UserConverter {
    UserDto convertEntityToResponseDto(UserEntity userEntity);

    UserEntity convertRequestDtoToEntity(UserDto dto);

}
