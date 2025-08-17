package com.alphasoft.EMS.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jti")
    private String jti;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @Column(name = "revoked", nullable = false)
    private boolean revoked;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "logout_time")
    private Instant logoutTime;
}

