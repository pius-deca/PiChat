package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.ProfilePicDTO;
import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping
public class ProfileController {
  private ProfileService profileService;
  private ModelMapper modelMapper;

  public ProfileController(ProfileService profileService, ModelMapper modelMapper) {
    this.profileService = profileService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/user/profile")
  public ResponseEntity<ApiResponse<ProfilePic>> updateProfile(@Valid @RequestBody ProfilePicDTO profile, HttpServletRequest request){
    ProfilePic newProfile = profileService.uploadProfile(modelMapper.map(profile, ProfilePic.class), request);
    ApiResponse<ProfilePic> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newProfile);
    response.setMessage("A user has updated is profile picture");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/user/profile/{pic}")
  public ResponseEntity<ApiResponse<String>> deleteProfile(@PathVariable(name = "pic") String profile, HttpServletRequest request) throws Exception {
    profileService.delete(profile, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Profile picture has been deleted");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
