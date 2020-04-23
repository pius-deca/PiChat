package com.github.pius.pichats.service;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.ProfileRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService{

  private JwtProvider jwtProvider;
  private ProfileRepository profileRepository;
  private UserRepository userRepository;
  private CloudService cloudService;

  @Autowired
  public ProfileServiceImpl(JwtProvider jwtProvider, ProfileRepository profileRepository, UserRepository userRepository, CloudService cloudService) {
    this.jwtProvider = jwtProvider;
    this.profileRepository = profileRepository;
    this.userRepository = userRepository;
    this.cloudService = cloudService;
  }

  @Override
  public ProfilePic uploadProfile(ProfilePic pic, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      ProfilePic profile = new ProfilePic();
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          Optional<ProfilePic> profilePic = profileRepository.findByUser(userByUsername.get());
          // upload post if username exists
          cloudService.upload(pic.getProfilePic());
          if (profilePic.isPresent()){
            // delete the previous profile before uploading a new one
            cloudService.deleteFile(profilePic.get().getProfilePic());
            profilePic.get().setProfilePic(cloudService.fileName);
            return profileRepository.save(profilePic.get());
          }
          profile.setProfilePic(cloudService.fileName);
          profile.setUser(userByUsername.get());
          return profileRepository.save(profile);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        Optional<ProfilePic> profilePic = profileRepository.findByUser(userByEmail.get());
        // upload post if username exists
        cloudService.upload(pic.getProfilePic());
        if (profilePic.isPresent()){
          // delete the previous profile before uploading a new one
          cloudService.deleteFile(profilePic.get().getProfilePic());
          profilePic.get().setProfilePic(cloudService.fileName);
          return profileRepository.save(profilePic.get());
        }
        profile.setProfilePic(cloudService.fileName);
        profile.setUser(userByEmail.get());
        return profileRepository.save(profile);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
