package com.alphasoft.EMS.dto;

import com.alphasoft.EMS.enums.InvitationStatus;
import com.alphasoft.EMS.model.FamilyInvitation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FamilyInvitationResponse {
    
    private Long id;
    private String theInviterUsername;
    private String theInvitedUsername;
    private String familyName;
    private String familyCode;
    private String message;
    private InvitationStatus status;
    private LocalDate createdAt;
    private LocalDate expiresAt;
    private LocalDate respondedAt;
} 