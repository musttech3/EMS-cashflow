package com.alphasoft.EMS.service;

import com.alphasoft.EMS.dto.FamilyResponse;
import com.alphasoft.EMS.enums.FamilyRole;
import com.alphasoft.EMS.exception.FamilyNotFoundException;
import com.alphasoft.EMS.exception.RelationshipException;
import com.alphasoft.EMS.model.Family;
import com.alphasoft.EMS.model.UserFamily;
import com.alphasoft.EMS.repository.UserFamilyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class UserFamilyService {


    private final UserService userService;
    private final ProfileService profileService;
    private final FamilyService familyService;
    private final UserFamilyRepository userFamilyRepository;

    @Autowired
    public UserFamilyService(
            UserService userService,
            ProfileService profileService,
            FamilyService familyService,
            UserFamilyRepository userFamilyRepository
    ) {
        this.userService = userService;
        this.profileService = profileService;
        this.familyService = familyService;
        this.userFamilyRepository = userFamilyRepository;
    }

    public void save(UserFamily userFamily){

        userFamilyRepository.save(userFamily);
    }

    public UserFamily findById(Long id){

        return userFamilyRepository.findById(id).orElse(null);
    }

    public List<UserFamily> findAll(){
        return userFamilyRepository.findAll();
    }

    public void delete(UserFamily userFamily){
        userFamilyRepository.delete(userFamily);
    }

    public void addMemberToFamily(Long userId, Long familyId) {

        if (userService.findUserById(userId) == null){
            throw new UsernameNotFoundException("User not found");
            
        } else if (familyService.findById(familyId) == null){
            throw new FamilyNotFoundException("Family not found");
            
        } else if(isUserFromThisFamily(userId, familyId)){
            throw new RelationshipException("You already have a relationship with this family");
            
        }

        UserFamily userFamily = UserFamily.builder()
                .user(userService.findUserById(userId))
                .family(familyService.findById(familyId))
                .role(FamilyRole.MEMBER)
                .joinDate(LocalDate.now())
                .build();
        userFamilyRepository.save(userFamily);
    }

    public void removeMemberFromFamily(Long userId, Long familyId) {

        if (userService.findUserById(userId) == null){

            throw new UsernameNotFoundException("User not found");
        }
        else if (familyService.findById(familyId) == null){

            throw new FamilyNotFoundException("Family not found");
        }
        else if(!isUserFromThisFamily(userId, familyId)){

            throw new RelationshipException("Maybe you don't have a relationship with this family");
        }
        
        userFamilyRepository.deleteByUserIdAndFamilyId(userId, familyId);
    }

    public List<FamilyResponse> getUserFamilies(Long userId) {

        if (userService.findUserById(userId) == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<FamilyResponse> families = new ArrayList<>();
        for (Family family : userFamilyRepository.findUserFamiliesByUserId(userId)) {

            FamilyResponse familyResponse = FamilyResponse.builder()
                    .id(family.getId())
                    .profileImage(family.getProfileImage())
                    .familyName(family.getFamilyName())
                    .build();
            families.add(familyResponse);
        }
        return families;
    }

    public List<Map<String, Object>> getFamilyMembers(Long familyId){

        List<Map<String, Object>> members = new ArrayList<>();

        for (UserFamily userFamily : userFamilyRepository.findFamilyMembersByFamilyId(familyId)){

            Map<String, Object> extra = new HashMap<>();
            extra.put("id", userFamily.getUser().getId().toString());
            extra.put("profileImage", profileService.findProfileByUserId(
                    userFamily.getUser().getId()
                    )
                    .getImage()
            );
            extra.put("username", userFamily.getUser().getUsername());
            extra.put("firstName", userFamily.getUser().getFirstName());
            extra.put("lastName", userFamily.getUser().getLastName());
            members.add(extra);
        }
        return members;
    }

    public boolean isUserFromThisFamily(Long userId, Long familyId) {

        if (userService.findUserById(userId) == null){
            throw new UsernameNotFoundException("User not found");
        } else {
            return userFamilyRepository.findByUserIdAndFamilyId(userId, familyId).isPresent();
        }
    }

    public boolean isAdmin(Long userId, Long familyId){

        if (userService.findUserById(userId) == null){

            throw new UsernameNotFoundException("User not found");
        } else {

            UserFamily  userFamily = userFamilyRepository.findByUserIdAndFamilyId(userId, familyId).orElse(null);
            if (userFamily == null){
                throw new RelationshipException("You`re not from the family");
            }
            return userFamily.getRole() == FamilyRole.ADMIN;
        }
    }

    public UserFamily findByUserIdAndFamilyId(Long userId, Long familyId){

        return userFamilyRepository.findByUserIdAndFamilyId(userId, familyId).orElse(null);
    }

//    public void addMemberToFamily(Long userId, Long groupId) {
//        User user = userRepository.findById(userId).orElseThrow(
//                () -> new UsernameNotFoundException("User not found")
//        );
//        Family family = familyRepository.findById(groupId).orElseThrow(
//                () -> new FamilyNotFoundException("Family not found")
//        );
//
//        user.getFamilies().add(family);
//        family.getUsers().add(user);
//
//        userRepository.save(user); // يكفي لحفظ العلاقة
//    }
//
//    public void removeMemberFromFamily(Long userId, Long groupId) {
//        User user = userRepository.findById(userId).orElseThrow();
//        Family family = familyRepository.findById(groupId).orElseThrow();
//
//        user.getFamilies().remove(family);
//        family.getUsers().remove(user);
//
//        userRepository.save(user); // يكفي لحفظ التغييرات
//    }

//    public Set<FamilyResponse> getUserFamilies(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(
//                () -> new UsernameNotFoundException("User not found")
//        );
//        Set<FamilyResponse> familyResponse = Set.of();
//        for (Family family : user.getFamilies()) {
//            familyResponse.add(
//                    new FamilyResponse(
//                            family.getId(),
//                            family.getProfileImage(),
//                            family.getFamilyName()
//                    )
//            );
//        }
//        return familyResponse;
//    }
//
//    public Set<User> getFamilyMembers(Long groupId) {
//        Family family = familyRepository.findById(groupId).orElseThrow();
//        return family.getUsers(); // يعيد Set<User>
//    }
//
//    public boolean isUserInFamily(Long userId, Long familyId) {
//        User user = userRepository.findById(userId).orElseThrow();
//        return user.getFamilies().stream()
//                .anyMatch(family -> family.getId() == familyId
//                );
//    }




}
