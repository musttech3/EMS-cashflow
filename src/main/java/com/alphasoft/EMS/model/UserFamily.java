package com.alphasoft.EMS.model;


import com.alphasoft.EMS.enums.FamilyRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_families")
public class UserFamily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "family_id")
    private Family family;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private FamilyRole role;

    @Column(name = "join_date")
    private LocalDate joinDate;


}
