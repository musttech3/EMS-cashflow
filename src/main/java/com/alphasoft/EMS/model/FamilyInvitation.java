package com.alphasoft.EMS.model;

import com.alphasoft.EMS.enums.InvitationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
//@ToString(exclude = {"inviter", "invitee", "family"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "family_invitations")
public class FamilyInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "inviter_id", referencedColumnName = "id")
    private User theInviter;

    @ManyToOne
    @JoinColumn(name = "invited_id", referencedColumnName = "id")
    private User theInvited;

    @ManyToOne
    @JoinColumn(name = "family_id", referencedColumnName = "id")
    private Family family;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InvitationStatus status;

    @Column(name = "message")
    private String message;

    @Column(name = "expire_date")
    private LocalDate expireDate;

    @Column(name = "response_date")
    private LocalDate responseDate;



} 