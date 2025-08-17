package com.alphasoft.EMS.repository;

import com.alphasoft.EMS.model.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    @Query("SELECT f FROM Family f WHERE f.familyCode = :familyCode")
    Optional<Family> findByFamilyCode(@Param("familyCode") String familyCode);

}
