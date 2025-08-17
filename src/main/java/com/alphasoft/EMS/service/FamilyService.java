package com.alphasoft.EMS.service;

import com.alphasoft.EMS.exception.FamilyNotFoundException;
import com.alphasoft.EMS.model.Family;
import com.alphasoft.EMS.repository.FamilyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class FamilyService {

    FamilyRepository familyRepository;

    @Autowired
    public FamilyService(FamilyRepository familyRepository){
        this.familyRepository = familyRepository;
    }

    public Family findById(long id){
        return familyRepository.findById(id).orElseThrow(
                () ->new FamilyNotFoundException("Cannot find the family")
        );
    }

    public Family findByFamilyCode(String familyCode){
        return familyRepository.findByFamilyCode(familyCode).orElseThrow(
                () ->new FamilyNotFoundException("An expired or invalid code")
        );
    }

    public List<Family> findAll(){
        return familyRepository.findAll();
    }

    public void save(Family family){
        familyRepository.save(family);
    }

    public void deleteById(long id){
        familyRepository.deleteById(id);
    }

    public void delete(Family family){
        familyRepository.delete(family);
    }

}
