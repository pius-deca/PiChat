package com.github.pius.pichats.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.service.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class ProfileController {
  private final ProfileService profileService;

  @Autowired
  public ProfileController(ProfileService profileService) {
    this.profileService = profileService;
  }

  @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<Object>> updateProfile(@Valid @RequestParam("file") MultipartFile file,
      HttpServletRequest request) {
    Object newProfile = profileService.uploadProfile(file, request);
    ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newProfile);
    response.setMessage("Profile picture uploaded successfully!");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<ProfilePic>> findProfile(HttpServletRequest request) {
    ProfilePic profile = profileService.find(request);
    ApiResponse<ProfilePic> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(profile);
    response.setMessage("Profile picture found");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/profile")
  public ResponseEntity<ApiResponse<String>> deleteProfile(@RequestParam(name = "pic") String profile,
      HttpServletRequest request) throws Exception {
    profileService.delete(profile, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Profile picture has been deleted");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
