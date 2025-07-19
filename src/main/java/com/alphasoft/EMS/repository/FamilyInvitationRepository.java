package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.FamilyInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FamilyInvitationRepository extends JpaRepository<FamilyInvitation, Long> {

    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.theInvited.username = :username AND fi.status = 'PENDING'")
    List<FamilyInvitation> findPendingInvitationsByInvitedUsername(@Param("username") String username);

    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.theInviter.username = :username")
    List<FamilyInvitation> findInvitationsByInviterUsername(@Param("username") String username);

    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.theInvited.username = :username")
    List<FamilyInvitation> findInvitationsByInvitedUsername(@Param("username") String username);

//    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.family.id = :familyId")
//    List<FamilyInvitation> findInvitationsByFamilyId(@Param("familyId") Long familyId);

//    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.invitationCode = :invitationCode")
//    Optional<FamilyInvitation> findByInvitationCode(@Param("invitationCode") String invitationCode);

    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.theInvited.id = :theInvitedId AND fi.family.id = :familyId AND fi.status = 'PENDING'")
    Optional<FamilyInvitation> findPendingInvitationByInvitedAndFamily(@Param("theInvitedId") Long theInvitedId, @Param("familyId") Long familyId);

    @Modifying
    @Query("DELETE FROM FamilyInvitation fi WHERE fi.theInvited.id = :theInvitedId AND fi.family.id = :familyId")
    public void deleteByInvitedIdAndFamilyId(@Param("theInvitedId")Long theInvitedId, @Param("familyId") Long familyId);


    @Query("SELECT fi FROM FamilyInvitation fi WHERE fi.expireDate < :now AND fi.status = 'PENDING'")
    List<FamilyInvitation> findExpiredInvitations(@Param("now") LocalDate now);
} 