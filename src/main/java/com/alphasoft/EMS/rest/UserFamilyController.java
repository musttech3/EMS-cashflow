package com.alphasoft.EMS.rest;

import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.enums.FamilyRole;
import com.alphasoft.EMS.enums.InvitationStatus;
import com.alphasoft.EMS.exception.AccessDeniedException;
import com.alphasoft.EMS.exception.UserNotEnabledException;
import com.alphasoft.EMS.model.Family;
import com.alphasoft.EMS.model.FamilyInvitation;
import com.alphasoft.EMS.model.User;
import com.alphasoft.EMS.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserFamilyController {

    private final UserService userService;
    private final FamilyService familyService;
    private final UserFamilyService userFamilyService;
    private final AuthenticationService authenticationService;
    private final FamilyInvitationService familyInvitationService;

//    @Autowired
//    public UserFamilyController(
//            UserService userService,
//            FamilyService familyService,
//            UserFamilyService userFamilyService,
//            AuthenticationService authenticationService
//    ) {
//        this.userService = userService;
//        this.familyService = familyService;
//        this.userFamilyService = userFamilyService;
//        this.authenticationService = authenticationService;
//    }

    
    /*
        This method for adding a new family member by not expired family code
        User must be enabled, then invitation status becomes ACCEPTED & it is deleted
     */
    @PostMapping("/user_family/{familyCode}") // Test success
    public ResponseEntity<String> addMemberToFamily(
            @PathVariable String familyCode,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        Family family = familyService.findByFamilyCode(familyCode);
        if (!userResponse.isActive()){

            throw new UserNotEnabledException("The current user is disabled");
        }
        else if (userFamilyService.isUserFromThisFamily(userResponse.getId(), family.getId())){

            return ResponseEntity.ok().body(
                    "You already a member of this family"
            );
        }

        FamilyInvitation invitation = familyInvitationService.findPendingInvitationByInvitedAndFamily(userResponse.getId(), family.getId());
        if (invitation == null){

            throw new RuntimeException("This invitation is no longer valid");
        }
        else if ((invitation.getStatus().equals(InvitationStatus.REJECTED) || invitation.getStatus().equals(InvitationStatus.EXPIRED))){

            throw new RuntimeException("This invitation is no longer valid");
        }

        try {

            invitation.setStatus(InvitationStatus.ACCEPTED);
            familyInvitationService.saveInvitation(invitation);
            familyInvitationService.cleanupAcceptedOrRejectedInvitations(invitation.getTheInviter().getUsername(), userResponse.getUsername());
            userFamilyService.addMemberToFamily(userResponse.getId(), family.getId());
        } catch (Exception ex) {

            invitation.setStatus(InvitationStatus.PENDING);
            familyInvitationService.saveInvitation(invitation);
            userFamilyService.removeMemberFromFamily(userResponse.getId(), family.getId());
            return ResponseEntity.badRequest().body("Something wrong!, please try again later");
        }

        return ResponseEntity.ok().body(
                "Now you`re a member of the family"
        );
    }

    @DeleteMapping("user_family/{familyId}/{username}") // Test success
    public ResponseEntity<String> removeMemberFormFamily(
            @PathVariable Long familyId,
            @PathVariable String username,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        if (!userResponse.isActive()){

            throw new UserNotEnabledException("User not enabled");
        }
        else if (!userFamilyService.isUserFromThisFamily(userResponse.getId(), familyId)){

            return ResponseEntity.ok().body(
                   "You don`t have a relationship with this family"
            );
        }
        else if (userFamilyService.findByUserIdAndFamilyId(userResponse.getId(), familyId).getRole().equals(FamilyRole.MEMBER)) {

            throw new AccessDeniedException("You`re not one of the family admins");
        }

        User user = userService.findByUsername(username);
        userFamilyService.removeMemberFromFamily(user.getId(), familyId);

        return ResponseEntity.ok().body(
                "Remove successful"
        );
    }

}
