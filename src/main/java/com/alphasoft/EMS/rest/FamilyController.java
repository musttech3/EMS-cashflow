package com.alphasoft.EMS.rest;


import com.alphasoft.EMS.dto.FamilyCreationRequest;
import com.alphasoft.EMS.dto.FamilyResponse;
import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.enums.FamilyRole;
import com.alphasoft.EMS.enums.Role;
import com.alphasoft.EMS.exception.FamilyNotFoundException;
import com.alphasoft.EMS.exception.NoContentException;
import com.alphasoft.EMS.exception.UserNotEnabledException;
import com.alphasoft.EMS.model.Family;
import com.alphasoft.EMS.model.UserFamily;
import com.alphasoft.EMS.service.AuthenticationService;
import com.alphasoft.EMS.service.FamilyService;
import com.alphasoft.EMS.service.UserFamilyService;
import com.alphasoft.EMS.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FamilyController {

    private final FamilyService familyService;
    private final UserFamilyService userFamilyService;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public FamilyController(FamilyService familyService, UserFamilyService userFamilyService, UserService userService, AuthenticationService authenticationService) {
        this.familyService = familyService;
        this.userFamilyService = userFamilyService;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/families")  // Test success
    public ResponseEntity<String> createNewFamily(
            @RequestBody FamilyCreationRequest creationRequest,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));

        if (!userResponse.isActive()){

            throw new UserNotEnabledException("You`re not enabled to use CashFlow");
        }

        String familyCode = new BCryptPasswordEncoder().encode(LocalDateTime.now().toString() + userResponse.getUsername() + creationRequest.getFamilyName()).substring(7,36);
        System.out.println(familyCode);
        Family newFamily = Family.builder()
                .familyName(creationRequest.getFamilyName())
                .profileImage(creationRequest.getProfileImage())
                .description(creationRequest.getDescription())
                .creator(userService.findUserById(userResponse.getId()))
                .familyCode(familyCode)
                .creationDate(LocalDate.now())
                .build();

        try{

            familyService.save(newFamily);
        }catch (Exception ex){

            return ResponseEntity.status(403).body("Something wrong! please try again later");
        }

        UserFamily userFamily = UserFamily.builder()
                .user(userService.findUserById(userResponse.getId()))
                .family(newFamily)
                .role(FamilyRole.ADMIN)
                .joinDate(LocalDate.now())
                .build();
        try{

            userFamilyService.save(userFamily);
        }catch (Exception ex){
            familyService.delete(newFamily);
            return ResponseEntity.status(403).body("Something wrong! please try again later");
        }

        return ResponseEntity.ok().body("The family created successfully");
    }

    @GetMapping("/families") // Test success
    public ResponseEntity<List<Family>> getAllFamilies(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));

        if (userResponse.getRole().equals(Role.USER)) {

            throw new NoContentException("You`ve not permissions to access");
        }
        return ResponseEntity.ok().body(familyService.findAll());
    }

    @GetMapping("/families/{id}") // Test success
    public ResponseEntity<Map<String, Object>> getFamilyById(
            @PathVariable Long id,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        Map<String, Object> familyResponseData = new HashMap<>();
        List<Map<String, Object>> familyMembers = new ArrayList<>();
        Family family = familyService.findById(id);

        if (!userResponse.isActive()){

            throw new UserNotEnabledException("User not enabled");
        }
        else if (family == null) {

            throw new FamilyNotFoundException("Family not found");
        }
        else if (!userFamilyService.isUserFromThisFamily(userResponse.getId(), id) || !Role.ADMIN.equals(userResponse.getRole())) {

            throw new NoContentException("No content, You`re not from this family");
        }

        familyResponseData.put("id", family.getId());
        familyResponseData.put("profileImage", family.getProfileImage());
        familyResponseData.put("familyName", family.getFamilyName());
        familyResponseData.put("description", family.getDescription());
        if (FamilyRole.ADMIN.equals(userFamilyService.findByUserIdAndFamilyId(userResponse.getId(), id).getRole())){
            familyResponseData.put("familyCode", family.getFamilyCode());
        } else {
            familyResponseData.put("familyCode", null);
        }
        familyResponseData.put("creationDate", family.getCreationDate());
        familyResponseData.put("creator", family.getCreator().getUsername());

        for (Map<String, Object> member : userFamilyService.getFamilyMembers(id)) {
            if (userService.isActive(Long.parseLong(member.get("id").toString()))) {

                familyMembers.add(member);
            }
        }
        familyResponseData.put("members", familyMembers);


        return ResponseEntity.ok().body(familyResponseData);
    }

    @GetMapping("/families/{code}") // Test success
    public ResponseEntity<Map<String, Object>> getFamilyByCode(
            @PathVariable String code,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        Map<String, Object> familyResponseData = new HashMap<>();
        List<Map<String, Object>> familyMembers = new ArrayList<>();
        Family family = familyService.findByFamilyCode(code);

        if (!userResponse.isActive()){

            throw new UserNotEnabledException("The current user is disabled");
        }
        else if (family == null) {

            throw new FamilyNotFoundException("There is no family with this code");
        }

        familyResponseData.put("id", family.getId());
        familyResponseData.put("profileImage", family.getProfileImage());
        familyResponseData.put("familyName", family.getFamilyName());
        familyResponseData.put("description", family.getDescription());
        familyResponseData.put("familyCode", family.getFamilyCode());
        familyResponseData.put("creationDate", family.getCreationDate());
        familyResponseData.put("creator", family.getCreator().getUsername());

        for (Map<String, Object> member : userFamilyService.getFamilyMembers(family.getId())) {
            if (userService.isActive(Long.parseLong(member.get("id").toString()))) {

                familyMembers.add(member);
            }
        }
        familyResponseData.put("members", familyMembers);


        return ResponseEntity.ok().body(familyResponseData);
    }

    @GetMapping("/families/my_families") // Test success
    public ResponseEntity<List<FamilyResponse>> getMyFamilies(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        if (!userService.findUserById(userResponse.getId()).isActive()){
                throw new UserNotEnabledException("The current user is disabled");
        }
        return ResponseEntity.ok().body(
                userFamilyService.getUserFamilies(userResponse.getId())
        );
    }





}
