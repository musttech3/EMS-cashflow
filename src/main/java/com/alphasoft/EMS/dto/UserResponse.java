package com.alphasoft.EMS.dto;

import com.alphasoft.EMS.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
    private boolean active;

}
