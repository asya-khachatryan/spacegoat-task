package com.spacegoat.task.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @Column(name = "username", unique = true)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Column(name = "city")
    @NotBlank(message = "City is mandatory")
    private String city;

    @Column(name = "balance")
    @NotNull
    private Double balance;
}
