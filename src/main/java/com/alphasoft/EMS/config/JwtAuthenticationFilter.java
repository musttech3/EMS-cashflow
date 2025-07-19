package com.alphasoft.EMS.config;

import com.alphasoft.EMS.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Injecting the JwtService to handle token operations (like extracting username, validating token)
    private final JwtService jwtService;

    // Injecting UserDetailsService to load user data from database
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Extract the Authorization header from the request
            final String authHeader = request.getHeader("Authorization");

            // If the Authorization header is missing or doesn't start with "Bearer ",
            // then skip the JWT check and continue the request chain
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Remove "Bearer " prefix and get the token only
            final String jwt = authHeader.substring(7);
            // Extract the username (subject) from the JWT token
            final String username = jwtService.extractUsername(jwt);

            // Check if username is not null and there is no authentication set yet in the context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Load user details (username, password, roles) from database or in-memory store
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // Validate the token using user information (to ensure it's not expired or tampered with)
                if (jwtService.isTokenValid(jwt, userDetails, request.getHeader("User-Agent"))) {

                    // Create an authentication token that Spring Security will recognize
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,              // principal (the user object)
                            null,                     // credentials (null because we’re using JWT not password)
                            userDetails.getAuthorities()  // roles/authorities for the user
                    );

                    // Set request-specific details like IP address, session ID, etc.
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Set the authentication into the security context so Spring Security knows
                    // this user is authenticated for the current request
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (ExpiredJwtException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        } catch (SignatureException ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token signature");
            return;
        }

        // Continue the request chain (proceed to the next filter or controller)
        filterChain.doFilter(request, response);
    }
}

