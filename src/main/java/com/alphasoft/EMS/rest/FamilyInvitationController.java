package com.alphasoft.EMS.rest;

import com.alphasoft.EMS.dto.FamilyInvitationRequest;
import com.alphasoft.EMS.dto.FamilyInvitationResponse;
import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.enums.InvitationStatus;
import com.alphasoft.EMS.exception.AccessDeniedException;
import com.alphasoft.EMS.exception.FamilyNotFoundException;
import com.alphasoft.EMS.exception.RelationshipException;
import com.alphasoft.EMS.exception.UserNotEnabledException;
import com.alphasoft.EMS.model.Family;
import com.alphasoft.EMS.model.FamilyInvitation;
import com.alphasoft.EMS.model.User;
import com.alphasoft.EMS.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class FamilyInvitationController {

    private final FamilyInvitationService familyInvitationService;
    private final AuthenticationService authenticationService;
    private final UserFamilyService userFamilyService;
    private final FamilyService familyService;
    private final UserService userService;

    @PostMapping("/invitations") // Test success
    public ResponseEntity<String> sendInvitation(
            @RequestBody FamilyInvitationRequest invitationRequest,
            @CookieValue(value = "CashFlow-EMS-JWT")String jwt,
            HttpServletRequest httpRequest
            ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        Family theFamily = familyService.findById(invitationRequest.getFamilyId());
        User theInvited = userService.findByUsername(invitationRequest.getTheInvitedUsername());

        if (!userResponse.isActive()){

            throw new UserNotEnabledException("You`re not enabled to use CashFlow"); // Done
        }
        else if (theInvited == null){

            throw new UsernameNotFoundException("Cannot find the invited user"); // Done
        }
        else if (!theInvited.isActive()) {

            throw new UserNotEnabledException("The invited user is not enabled to use CashFlow"); // Done
        }
        else if (!userFamilyService.isUserFromThisFamily(userResponse.getId(), invitationRequest.getFamilyId())){

            throw new RelationshipException("You`re not from the family"); // Done
        }
        else if (userFamilyService.isUserFromThisFamily(theInvited.getId(), invitationRequest.getFamilyId())){

            return ResponseEntity.ok().body("The user already a member of the family"); // Done
        }
        else if (!userFamilyService.isAdmin(userResponse.getId(), invitationRequest.getFamilyId())) {

            throw new AccessDeniedException("You`re not one of the family admins"); // Done
        }

        familyInvitationService.deleteByInvitedIdAndFamilyId(theInvited.getId(), theFamily.getId());

        FamilyInvitation invitation = FamilyInvitation.builder()
                .theInviter(userService.findUserById(userResponse.getId()))
                .theInvited(theInvited)
                .family(theFamily)
                .status(InvitationStatus.PENDING)
                .message(invitationRequest.getMessage())
                .expireDate(LocalDate.now().plusWeeks(1))
                .build();

        familyInvitationService.saveInvitation(invitation);
        return ResponseEntity.ok().body("The invitation has been sent");
    }

    @PutMapping("invitations/reject/{familyCode}") // Test success
    public ResponseEntity<String> rejectInvitation(
            @PathVariable String familyCode,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        Family family = familyService.findByFamilyCode(familyCode);
        if (family == null){
            return ResponseEntity.ok().body("Family code has been changed");
        }

        FamilyInvitation invitation = familyInvitationService.findPendingInvitationByInvitedAndFamily(userResponse.getId(), family.getId());
        if (invitation == null){
            return ResponseEntity.ok().body("The invitation is already expired");
        }

        invitation.setStatus(InvitationStatus.REJECTED);
        familyInvitationService.cleanupAcceptedOrRejectedInvitations(invitation.getTheInviter().getUsername(), userResponse.getUsername());
        return ResponseEntity.ok().body("The invitation rejected successfully");
    }

    @GetMapping("/invitations/sent_invitations") // Test success
    public ResponseEntity<List<FamilyInvitationResponse>> getSentInvitations(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        List<FamilyInvitationResponse> sentInvitations = new ArrayList<>();
        for (FamilyInvitation familyInvitation : familyInvitationService
                .findInvitationsByInviterUsername(userResponse.getUsername())
        ){

            FamilyInvitationResponse response = FamilyInvitationResponse.builder()
                    .theInviterUsername(userResponse.getUsername())
                    .theInvitedUsername(familyInvitation.getTheInvited().getUsername())
                    .familyName(familyInvitation.getFamily().getFamilyName())
                    .familyCode(familyInvitation.getFamily().getFamilyCode())
                    .message(familyInvitation.getMessage())
                    .status(familyInvitation.getStatus())
                    .expiresAt(familyInvitation.getExpireDate())
                    .respondedAt(familyInvitation.getResponseDate())
                    .build();
            sentInvitations.add(response);
        }

        return ResponseEntity.ok().body(sentInvitations);
    }

    @GetMapping("/invitations/received_invitations") // Test success
    public ResponseEntity<List<FamilyInvitationResponse>> getReceivedInvitations(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));

        List<FamilyInvitationResponse> receivedInvitations = new ArrayList<>();
        for (FamilyInvitation familyInvitation : familyInvitationService
                .findPendingInvitationsByInvitedUsername(userResponse.getUsername())
        ){

            FamilyInvitationResponse response = FamilyInvitationResponse.builder()
                    .theInviterUsername(familyInvitation.getTheInviter().getUsername())
                    .theInvitedUsername(familyInvitation.getTheInvited().getUsername())
                    .familyName(familyInvitation.getFamily().getFamilyName())
                    .familyCode(familyInvitation.getFamily().getFamilyCode())
                    .message(familyInvitation.getMessage())
                    .status(familyInvitation.getStatus())
                    .expiresAt(familyInvitation.getExpireDate())
                    .respondedAt(familyInvitation.getResponseDate())
                    .build();
            receivedInvitations.add(response);

        }
        return ResponseEntity.ok().body(receivedInvitations);
    }

}
