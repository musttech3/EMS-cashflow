package com.alphasoft.EMS.service;

import com.alphasoft.EMS.model.Profile;
import com.alphasoft.EMS.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    ProfileRepository profileRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository){
        this.profileRepository = profileRepository;
    }

    public Profile findById(long id){
        return profileRepository.findById(id).orElse(null);
    }

    public List<Profile> findAll(){
        return profileRepository.findAll();
    }

    public Profile findProfileByUserId(Long id){
        return profileRepository.findByUserId(id).orElse(null);
    }

    @Transactional
    public void save(Profile profile){
        profileRepository.save(profile);
    }

    @Transactional
    public void deleteById(long id){
        profileRepository.deleteById(id);
    }

}
