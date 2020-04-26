package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.dto.SearchUsernameDto;
import com.github.pius.pichats.dto.SignupRequestDTO;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.service.AuthService;
import com.github.pius.pichats.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/search")
  public ResponseEntity<ApiResponse<User>> signup(@Valid @RequestBody SearchUsernameDto username, HttpServletRequest request){
    User searchUser = userService.searchByUsername(username, request);
    ApiResponse<User> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(searchUser);
    response.setMessage("The user searched for retrieved successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
