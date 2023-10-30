package com.spacegoat.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    private String firstName;
    private String lastName;
    private String username;
    private String city;
    private Double balance;
}
