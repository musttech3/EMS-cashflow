package com.alphasoft.EMS.dto;

import com.alphasoft.EMS.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private final Role role = Role.USER;
    private byte[] profileImage;
    private String bio;
    private String job;
    private String birthDate;

}
