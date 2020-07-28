package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.*;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.service.*;
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
  private final UserService userService;
  private final MapValidationErrorService mapValidationErrorService;

  @Autowired
  public UserController(UserService userService, MapValidationErrorService mapValidationErrorService) {
    this.userService = userService;
    this.mapValidationErrorService = mapValidationErrorService;
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

//  @PatchMapping("/update_bio")
//  public ResponseEntity<ApiResponse<Bio>> updateUserBio(@Valid @RequestBody BioDTO bioDTO, HttpServletRequest request){
//    Bio updatedUserBio = userService.updateUserBio(bioDTO, request);
//    ApiResponse<Bio> response = new ApiResponse<>(HttpStatus.OK);
//    response.setMessage(updatedUserBio.getUser().getUsername()+" updated his/her bio successfully.");
//    response.setData(updatedUserBio);
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }
}
