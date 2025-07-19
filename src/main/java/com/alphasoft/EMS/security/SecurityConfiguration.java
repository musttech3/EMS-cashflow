package com.alphasoft.EMS.security;

import jakarta.servlet.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private Filter jwtAuthFilter;
    private AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfiguration(
            @Qualifier("jwtAuthenticationFilter") Filter jwtAuthFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable())// Disable CSRF using lambda-style
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/**")
                            .permitAll() // Allow public routes
                            .anyRequest()
                            .authenticated()                // Require auth for others
                    )
                    .sessionManagement(session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Use stateless JWT-based sessions
                    )
                    .authenticationProvider(authenticationProvider) // Your custom provider
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

            return http.build(); // Build the security chain
    }

}
