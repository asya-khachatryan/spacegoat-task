package com.spacegoat.task.converter.impl;

import com.spacegoat.task.converter.UserConverter;
import com.spacegoat.task.domain.User;
import com.spacegoat.task.dto.UserRequestDto;
import com.spacegoat.task.dto.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public UserResponseDto convertEntityToResponseDto(User user) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setFirstName(user.getFirstName());
        responseDto.setLastName(user.getLastName());
        responseDto.setUsername(user.getUsername());
        responseDto.setCity(user.getCity());
        responseDto.setBalance(user.getBalance());
        responseDto.setId(user.getId());
        return responseDto;
    }

    @Override
    public User convertRequestDtoToEntity(UserRequestDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setCity(dto.getCity());
        user.setBalance(dto.getBalance());
        return user;
    }
}
