package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.*;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
  private UserService userService;
  private BioService bioService;
  private ProfileService profileService;
  private FollowService followService;
  private ModelMapper modelMapper;
  private MapValidationErrorService mapValidationErrorService;

  @Autowired
  public UserController(UserService userService, BioService bioService, ProfileService profileService, FollowService followService, ModelMapper modelMapper, MapValidationErrorService mapValidationErrorService) {
    this.userService = userService;
    this.bioService = bioService;
    this.profileService = profileService;
    this.followService = followService;
    this.modelMapper = modelMapper;
    this.mapValidationErrorService = mapValidationErrorService;
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

  @GetMapping("/{username}")
  public ResponseEntity<ApiResponse<User>> search(@PathVariable(name = "username") String username, HttpServletRequest request){
    User searchUser = userService.searchByUsername(username, request);
    ApiResponse<User> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(searchUser);
    response.setMessage("The user searched for retrieved successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/found")
  public ResponseEntity<ApiResponse<List<User>>> listOfSearchedUser(@RequestParam String username, HttpServletRequest request){
    List<User> searchUsers = userService.searchUsernameByString(username, request);
    ApiResponse<List<User>> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(searchUsers);
    response.setMessage("The users searched for retrieved successfully");
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

  @GetMapping("/followers")
  public ResponseEntity<ApiResponse<Integer>> followers(HttpServletRequest request){
    int numOfFollowers = followService.countFollowers(request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowers);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/following")
  public ResponseEntity<ApiResponse<Integer>> following(HttpServletRequest request){
    int numOfFollowing = followService.countFollowing(request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowing);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{username}/followers")
  public ResponseEntity<ApiResponse<Integer>> followers(@PathVariable(name = "username") String username, HttpServletRequest request){
    int numOfFollowers = followService.countFollowersOfSearchedUser(username, request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowers);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{username}/following")
  public ResponseEntity<ApiResponse<Integer>> following(@PathVariable(name = "username") String username, HttpServletRequest request){
    int numOfFollowing = followService.countFollowingSearchedUser(username, request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowing);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/changePassword")
  public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changePasswordDTO, BindingResult bindingResult, HttpServletRequest request){
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null){
      return errorMap;
    }
    String message = userService.changePassword(changePasswordDTO, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping("/update_account")
  public ResponseEntity<ApiResponse<UpdateResponseDTO>> updateUser(@Valid @RequestBody UpdateRequestDTO updateRequestDTO, HttpServletRequest request){
    UpdateResponseDTO updatedUser = userService.updateUser(updateRequestDTO, request);
    ApiResponse<UpdateResponseDTO> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(updatedUser.getUsername()+" updated his/her account successfully.");
    response.setData(updatedUser);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PatchMapping("/update_bio")
  public ResponseEntity<ApiResponse<Bio>> updateUserBio(@Valid @RequestBody BioDTO bioDTO, HttpServletRequest request){
    Bio updatedUserBio = userService.updateUserBio(bioDTO, request);
    ApiResponse<Bio> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(updatedUserBio.getUser().getUsername()+" updated his/her bio successfully.");
    response.setData(updatedUserBio);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
