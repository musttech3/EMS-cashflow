package com.alphasoft.EMS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamilyResponse {

    private Long id;
    private byte[] profileImage;
    private String familyName;

}
