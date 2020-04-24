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

  // this method validates picture format
  protected String pictureFormat(String pic){
    if (pic.endsWith(".jpg") || pic.endsWith(".png")){
      return pic;
    }else{
      throw new CustomException("Profile picture must be picture format", HttpStatus.BAD_REQUEST);
    }
  }

  // this method get a particular profile
  protected ProfilePic getProfile(String pic) {
    try{
      Optional<ProfilePic> profileFound = profileRepository.findByProfilePic(pic);
      if (profileFound.isPresent()){
        return profileFound.get();
      }
      throw new CustomException("Post : "+pic+ " does not exists", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method uploads and edits profile
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
          cloudService.upload(pictureFormat(pic.getProfilePic()));
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
        cloudService.upload(pictureFormat(pic.getProfilePic()));
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

  // this method finds a profile that belongs to a user
  @Override
  public ProfilePic find(String pic, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      ProfilePic profile = getProfile(pic);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          if (profile.getUser().equals(userByUsername.get())){
            return profile;
          }
          throw new CustomException(identifier+ " does not have profile "+pic, HttpStatus.NOT_FOUND);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }
      if (profile.getUser().equals(userByEmail.get())) {
        return profile;
      }
      throw new CustomException(identifier+ " does not have profile "+pic, HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method deletes the profile that belongs to a user
  @Override
  public void delete(String profile, HttpServletRequest request) throws Exception {
    cloudService.deleteFile(profile);
    profileRepository.delete(find(profile, request));
  }
}
