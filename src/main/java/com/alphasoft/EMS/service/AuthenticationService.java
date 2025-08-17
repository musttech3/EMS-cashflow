package com.alphasoft.EMS.service;


import com.alphasoft.EMS.dto.LoginRequest;
import com.alphasoft.EMS.dto.RegisterRequest;
import com.alphasoft.EMS.dto.UserResponse;
import com.alphasoft.EMS.enums.FamilyRole;
import com.alphasoft.EMS.enums.Role;
import com.alphasoft.EMS.exception.InvalidJwtException;
import com.alphasoft.EMS.exception.UsernameAlreadyExistsException;
import com.alphasoft.EMS.model.Profile;
import com.alphasoft.EMS.model.User;
import com.alphasoft.EMS.model.Token;
import com.alphasoft.EMS.repository.ProfileRepository;
import com.alphasoft.EMS.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Service
public class AuthenticationService {


    private final UserRepository userRepository;

    private final ProfileRepository profileRepository;

    private final TokenService tokenService;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request, String userAgent) {
        User existUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (existUser != null) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(Role.USER)
                .joinDate(LocalDate.now())
                .active(true)
                .build();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Profile profile = Profile.builder()
                .user(user)
                .image(request.getProfileImage())
                .bio(request.getBio())
                .job(request.getJob())
                .birthdate(LocalDate.parse(request.getBirthDate(), formatter))
                .build();

        String newToken = jwtService.generateToken(user, request.getRole(), userAgent);
        tokenService.deleteOldTokens(user.getUsername(), userAgent);

        Token dBToken = Token.builder()
                .jti(jwtService.extractJti(newToken))
                .username(jwtService.extractUsername(newToken))
                .expiryDate(Instant.now().plus(30, ChronoUnit.DAYS))
                .userAgent(userAgent)
                .revoked(false)
                .build();

        userRepository.save(user);
        profileRepository.save(profile);
        tokenService.saveToken(dBToken);

        return newToken;
    }

    public String login(LoginRequest request, String userAgent) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        );

        String newToken = jwtService.generateToken(user, user.getRole(), userAgent);

        tokenService.deleteOldTokens(user.getUsername(), userAgent);

        Token dBToken = Token.builder()
                .jti(jwtService.extractJti(newToken))
                .username(jwtService.extractUsername(newToken))
                .expiryDate(Instant.now().plus(30, ChronoUnit.DAYS))
                .userAgent(userAgent)
                .revoked(false)
                .build();
        tokenService.saveToken(dBToken);

        return newToken;
    }

    public void logout(String jti, String username) {
        tokenService.revokeToken(jti, username);
    }

    public void logoutAll(String username) {
        tokenService.deleteTokensByUsername(username);
    }


    public UserResponse fetchAPI(String token, String userAgent) {
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if (!jwtService.isTokenValid(token, user, userAgent)) {
            throw new InvalidJwtException("Invalid JWT");
        }

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .active(user.isActive())
                .build();
    }
}
