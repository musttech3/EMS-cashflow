package com.alphasoft.EMS.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "family")
public class Family {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "family_name")
    private String familyName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "profile_image", columnDefinition = "LONGBLOB")
    private byte[] profileImage;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "creator", referencedColumnName = "id")
    private User creator;

    @Column(name = "family_code")
    private String familyCode;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserFamily> userFamilies = new ArrayList<>();

//    @ManyToMany(mappedBy = "families")
//    private Set<User> users;


}
