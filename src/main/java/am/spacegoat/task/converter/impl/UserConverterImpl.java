package am.spacegoat.task.converter.impl;

import am.spacegoat.task.converter.UserConverter;
import am.spacegoat.task.domain.UserEntity;
import am.spacegoat.task.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserDto convertEntityToResponseDto(UserEntity userEntity) {
        UserDto responseDto = new UserDto();
        responseDto.setFirstName(userEntity.getFirstName());
        responseDto.setLastName(userEntity.getLastName());
        responseDto.setUsername(userEntity.getUsername());
        responseDto.setCity(userEntity.getCity());
        responseDto.setBalance(userEntity.getBalance());
        responseDto.setId(userEntity.getId());
        return responseDto;
    }

    @Override
    public UserEntity convertRequestDtoToEntity(UserDto dto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(dto.getFirstName());
        userEntity.setLastName(dto.getLastName());
        userEntity.setUsername(dto.getUsername());
        userEntity.setCity(dto.getCity());
        userEntity.setBalance(dto.getBalance());
        return userEntity;
    }
}
