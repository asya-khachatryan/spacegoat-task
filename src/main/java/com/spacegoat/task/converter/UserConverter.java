package com.spacegoat.task.converter;

import com.spacegoat.task.domain.User;
import com.spacegoat.task.dto.UserRequestDto;
import com.spacegoat.task.dto.UserResponseDto;

public interface UserConverter {
    UserResponseDto convertEntityToResponseDto(User user);

    User convertRequestDtoToEntity(UserRequestDto dto);

}
