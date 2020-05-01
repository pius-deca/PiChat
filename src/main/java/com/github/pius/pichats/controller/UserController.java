package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.*;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.service.BioService;
import com.github.pius.pichats.service.FollowService;
import com.github.pius.pichats.service.ProfileService;
import com.github.pius.pichats.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
  private UserService userService;
  private BioService bioService;
  private ProfileService profileService;
  private FollowService followService;
  private ModelMapper modelMapper;

  @Autowired
  public UserController(UserService userService, BioService bioService, ProfileService profileService, FollowService followService, ModelMapper modelMapper) {
    this.userService = userService;
    this.bioService = bioService;
    this.profileService = profileService;
    this.followService = followService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/bio")
  public ResponseEntity<ApiResponse<Bio>> addBio(@Valid @RequestBody BioDTO bio, HttpServletRequest request){
    Bio newBio = bioService.addOrUpdate(modelMapper.map(bio, Bio.class), request);
    ApiResponse<Bio> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newBio);
    response.setMessage("A user has updated is bio");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/bio")
  public ResponseEntity<ApiResponse<Bio>> getBio(HttpServletRequest request){
    Bio foundBio = bioService.find(request);
    ApiResponse<Bio> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(foundBio);
    response.setMessage("User's bio retrieved successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/profile")
  public ResponseEntity<ApiResponse<ProfilePic>> updateProfile(@Valid @RequestBody ProfilePicDTO profile, HttpServletRequest request){
    ProfilePic newProfile = profileService.uploadProfile(modelMapper.map(profile, ProfilePic.class), request);
    ApiResponse<ProfilePic> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newProfile);
    response.setMessage("A user has updated is profile picture");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/profile/{pic}")
  public ResponseEntity<ApiResponse<String>> deleteProfile(@PathVariable(name = "pic") String profile, HttpServletRequest request) throws Exception {
    profileService.delete(profile, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Profile picture has been deleted");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}")
  public ResponseEntity<ApiResponse<User>> search(@PathVariable(name = "username") String username, HttpServletRequest request){
    User searchUser = userService.searchByUsername(username, request);
    ApiResponse<User> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(searchUser);
    response.setMessage("The user searched for retrieved successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}/follow")
  public ResponseEntity<ApiResponse<Follow>> follow(@PathVariable(name = "username") String username, HttpServletRequest request){
    Follow follow = followService.follow(username, request);
    ApiResponse<Follow> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(follow);
    response.setMessage("User has sent a request to follow '"+username+"'");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}/follow/accept")
  public ResponseEntity<ApiResponse<Follow>> acceptRequest(@PathVariable(name = "username") String username, HttpServletRequest request){
    Follow follow = followService.acceptRequest(username, request);
    ApiResponse<Follow> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(follow);
    response.setMessage("User has accepted '"+username+"' request");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}/follow/decline")
  public ResponseEntity<ApiResponse<String>> declineRequest(@PathVariable(name = "username") String username, HttpServletRequest request){
    String message = followService.declineRequest(username, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}/unfollow")
  public ResponseEntity<ApiResponse<String>> unFollow(@PathVariable(name = "username") String username, HttpServletRequest request){
    String message = followService.unFollow(username, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/followers")
  public ResponseEntity<ApiResponse<Integer>> followers(HttpServletRequest request){
    int numOfFollowers = followService.countFollowers(request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowers);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/following")
  public ResponseEntity<ApiResponse<Integer>> following(HttpServletRequest request){
    int numOfFollowing = followService.countFollowing(request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowing);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/changePassword")
  public ResponseEntity<ApiResponse<String>> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, HttpServletRequest request){
    String message = userService.changePassword(changePasswordDTO, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
