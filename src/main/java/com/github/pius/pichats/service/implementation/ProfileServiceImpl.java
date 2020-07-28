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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

  private final JwtProvider jwtProvider;
  private final ProfileRepository profileRepository;
  private final CloudService cloudService;

  @Autowired
  public ProfileServiceImpl(JwtProvider jwtProvider, ProfileRepository profileRepository, CloudService cloudService) {
    this.jwtProvider = jwtProvider;
    this.profileRepository = profileRepository;
    this.cloudService = cloudService;
  }

  // this method validates picture format
  protected static MultipartFile pictureFormat(MultipartFile pic){
    if (Objects.requireNonNull(pic.getOriginalFilename()).endsWith(".jpg") || pic.getOriginalFilename().endsWith(".JPG")
      || pic.getOriginalFilename().endsWith(".png") || pic.getOriginalFilename().endsWith(".PNG") || pic.getOriginalFilename().endsWith(".jpeg") || pic.getOriginalFilename().endsWith(".JPEG")){
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
  public Object uploadProfile(MultipartFile pic, HttpServletRequest request) {
    try{
      User user = jwtProvider.resolveUser(request);
      ProfilePic profile = new ProfilePic();
      Optional<ProfilePic> profilePic = profileRepository.findByUser(user);
      // upload post if username exists
      Object uploaded = cloudService.upload(pictureFormat(pic));
      if (profilePic.isPresent()){
        // delete the previous profile before uploading a new one
        cloudService.deleteFile(profilePic.get().getProfilePic());
        profilePic.get().setProfilePic(cloudService.getFileName());
        profilePic.get().setUrl(uploaded.toString());
        profileRepository.save(profilePic.get());
        return uploaded;
      }
      profile.setProfilePic(cloudService.getFileName());
      profile.setUser(user);
      profile.setUrl(uploaded.toString());
      profileRepository.save(profile);
      return uploaded;
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
