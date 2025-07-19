package com.alphasoft.EMS.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamilyInvitationRequest {

    private Long familyId;
    private String theInvitedUsername;
    private String message;
} 