package com.alphasoft.EMS.dto;

import com.alphasoft.EMS.model.Family;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private String username;
    private byte[] profileImage;
    private String firstName;
    private String lastName;
    private String bio;
    private String job;
    private LocalDate birthDate;
    private LocalDate joinDate;
    private List<FamilyResponse> families;

}
