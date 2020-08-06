package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.service.ProfileService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class ProfileController {
  private final ProfileService profileService;
  private final ModelMapper modelMapper;

  @Autowired
  public ProfileController(ProfileService profileService, ModelMapper modelMapper) {
    this.profileService = profileService;
    this.modelMapper = modelMapper;
  }

  @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<ApiResponse<Object>> updateProfile(@Valid @RequestParam("file") MultipartFile file, HttpServletRequest request){
    Object newProfile = profileService.uploadProfile(file, request);
    ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newProfile);
    response.setMessage("Profile picture uploaded successfully!");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/profile")
  public ResponseEntity<ApiResponse<String>> findProfile(HttpServletRequest request){
    String newProfile = profileService.find(request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(newProfile);
    response.setMessage("Profile picture found");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/profile")
  public ResponseEntity<ApiResponse<String>> deleteProfile(@RequestParam(name = "pic") String profile, HttpServletRequest request) throws Exception {
    profileService.delete(profile, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Profile picture has been deleted");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
