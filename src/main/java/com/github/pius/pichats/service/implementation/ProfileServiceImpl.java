package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.ProfileRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

  private JwtProvider jwtProvider;
  private ProfileRepository profileRepository;
  private CloudService cloudService;

  @Autowired
  public ProfileServiceImpl(JwtProvider jwtProvider, ProfileRepository profileRepository, CloudService cloudService) {
    this.jwtProvider = jwtProvider;
    this.profileRepository = profileRepository;
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
    try{
      User user = jwtProvider.resolveUser(request);
      ProfilePic profile = new ProfilePic();
      Optional<ProfilePic> profilePic = profileRepository.findByUser(user);
      // upload post if username exists
      Map uploaded = cloudService.upload(this.pictureFormat(pic.getProfilePic()));
      if (profilePic.isPresent()){
        // delete the previous profile before uploading a new one
        cloudService.deleteFile(profilePic.get().getProfilePic());
        profilePic.get().setProfilePic(cloudService.fileName);
        profilePic.get().setUrl((String) uploaded.get("secure_url"));
        return profileRepository.save(profilePic.get());
      }
      profile.setProfilePic(cloudService.fileName);
      profile.setUser(user);
      profile.setUrl((String) uploaded.get("secure_url"));
      return profileRepository.save(profile);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method finds a profile that belongs to a user
  @Override
  public ProfilePic find(String pic, HttpServletRequest request) {
    try{
      User user = jwtProvider.resolveUser(request);
      ProfilePic profile = this.getProfile(pic);
      if (profile.getUser().equals(user)){
        return profile;
      }
      throw new CustomException(user.getUsername()+ " does not have profile "+pic, HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method deletes the profile that belongs to a user
  @Override
  public void delete(String profile, HttpServletRequest request) throws Exception {
    cloudService.deleteFile(profile);
    profileRepository.delete(this.find(profile, request));
  }
}
