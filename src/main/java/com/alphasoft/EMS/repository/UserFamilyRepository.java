package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.Family;
import com.alphasoft.EMS.model.User;
import com.alphasoft.EMS.model.UserFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserFamilyRepository extends JpaRepository<UserFamily, Long> {

    @Query("SELECT u.family FROM UserFamily u WHERE u.user.id = :userId")
    List<Family> findUserFamiliesByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM UserFamily u WHERE u.family.id = :familyId")
    List<UserFamily> findFamilyMembersByFamilyId(@Param("familyId") Long familyId);

    @Query("SELECT u FROM UserFamily u WHERE u.user.id = :userId AND u.family.id = :familyId")
    Optional<UserFamily> findByUserIdAndFamilyId(Long userId, Long familyId);

    @Modifying
    @Query("DELETE FROM UserFamily u WHERE u.user.id = :userId AND u.family.id = :familyId")
    void deleteByUserIdAndFamilyId(Long userId, Long familyId);

}
