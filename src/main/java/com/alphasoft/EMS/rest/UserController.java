package com.alphasoft.EMS.rest;

import com.alphasoft.EMS.dto.FamilyResponse;
import com.alphasoft.EMS.dto.UserProfile;
import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.enums.Role;
import com.alphasoft.EMS.model.Profile;
import com.alphasoft.EMS.model.User;
import com.alphasoft.EMS.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    UserService userService;
    ProfileService profileService;
    UserFamilyService userFamilyService;
    AuthenticationService authenticationService;
    JwtService jwtService;

    @Autowired
    public UserController(
            UserService userService,
            ProfileService profileService,
            UserFamilyService userFamilyService,
            AuthenticationService authenticationService,
            JwtService jwtService
    ) {
        this.userService = userService;
        this.profileService = profileService;
        this.userFamilyService = userFamilyService;
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
    }

    @GetMapping("/users")  // Test success
    public ResponseEntity<List<User>> getAllUsers(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {

        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        if (userResponse.getRole().equals(Role.USER)) {

            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/users/me") // Test success
    public ResponseEntity<UserProfile> getMyProfile(
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        List<FamilyResponse> families = userFamilyService.getUserFamilies(userResponse.getId());
        User user = userService.findUserById(userResponse.getId());
        Profile profile = profileService.findProfileByUserId(userResponse.getId());

        if (profile == null){
            return ResponseEntity.noContent().build();
        }


        return ResponseEntity.ok().body(
                new UserProfile(
                        user.getUsername(),
                        profile.getImage(),
                        user.getFirstName(),
                        user.getLastName(),
                        profile.getBio(),
                        profile.getJob(),
                        profile.getBirthdate(),
                        user.getJoinDate(),
                        families
                )
        );
    }

    @GetMapping("/users/{username}") // Test success
    public ResponseEntity<UserProfile> getUserProfile(
            @PathVariable String username,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        User user = userService.findByUsername(username);
        Profile profile = profileService.findProfileByUserId(userResponse.getId());

        if (profile == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(
                new UserProfile(
                        user.getUsername(),
                        profile.getImage(),
                        user.getFirstName(),
                        user.getLastName(),
                        profile.getBio(),
                        profile.getJob(),
                        profile.getBirthdate(),
                        user.getJoinDate(),
                        null
                )
        );
    }

    @PutMapping("/users")
    public ResponseEntity<String> updateMe(
            @RequestBody User user,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        userService.saveUser(user);
        return ResponseEntity.ok().body(
                "User updated successfully"
        );
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<String> disableUser(
            @PathVariable String username,
            @CookieValue(value = "CashFlow-EMS-JWT") String jwt,
            HttpServletRequest httpRequest
    ) {
        UserResponse userResponse = authenticationService.fetchAPI(jwt, httpRequest.getHeader("User-Agent"));
        User user = userService.findByUsername(username);
        user.setActive(false);
        userService.saveUser(user);
        return ResponseEntity.ok().body(
                "User disabled successfully"
        );
    }

}
