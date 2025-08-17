package com.alphasoft.EMS.service;

import com.alphasoft.EMS.enums.InvitationStatus;
import com.alphasoft.EMS.model.FamilyInvitation;
import com.alphasoft.EMS.repository.FamilyInvitationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class FamilyInvitationService {

    final FamilyInvitationRepository familyInvitationRepository;

    public void saveInvitation(FamilyInvitation invitation){
        familyInvitationRepository.save(invitation);
    }

    public FamilyInvitation findInvitation(Long id){
        return familyInvitationRepository.findById(id).orElse(null);
    }

    public List<FamilyInvitation> findAllInvitations(){
        return familyInvitationRepository.findAll();
    }

    public FamilyInvitation findPendingInvitationByInvitedAndFamily(
            Long theInvited,
            Long familyId
    ) {
        return familyInvitationRepository.findPendingInvitationByInvitedAndFamily(theInvited, familyId).orElse(null);
    }

    @Transactional
    public void deleteById(Long id){
        familyInvitationRepository.deleteById(id);
    }

    @Transactional
    public void deleteInvitation(FamilyInvitation invitation) {
        familyInvitationRepository.delete(invitation);
    }

    @Transactional
    public void deleteByInvitedIdAndFamilyId(Long theInvitedId, Long familyId){
        familyInvitationRepository.deleteByInvitedIdAndFamilyId(theInvitedId, familyId);
    }

    public List<FamilyInvitation> findPendingInvitationsByInvitedUsername(String username) {
        return familyInvitationRepository
                .findPendingInvitationsByInvitedUsername(username);
    }

    public List<FamilyInvitation> findInvitationsByInviterUsername(String username) {
        return familyInvitationRepository
                .findInvitationsByInviterUsername(username);
    }

    public void cleanupExpiredInvitations() {

        for (FamilyInvitation invitation :
                familyInvitationRepository.findExpiredInvitations(LocalDate.now())
        ){

            invitation.setStatus(InvitationStatus.EXPIRED);
            familyInvitationRepository.delete(invitation);
        }

    }

    public void cleanupAcceptedOrRejectedInvitations(String theInviter, String theInvited) {

        for (FamilyInvitation invitation : familyInvitationRepository.findInvitationsByInviterUsername(theInviter)){

            if (invitation.getStatus().equals(InvitationStatus.ACCEPTED) || invitation.getStatus().equals(InvitationStatus.REJECTED)){

                familyInvitationRepository.delete(invitation);
            }
        }

        for (FamilyInvitation invitation : familyInvitationRepository.findInvitationsByInvitedUsername(theInvited)){

            if (invitation.getStatus().equals(InvitationStatus.ACCEPTED) || invitation.getStatus().equals(InvitationStatus.REJECTED)){

                familyInvitationRepository.delete(invitation);
            }
        }
    }



    

}
