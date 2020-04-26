package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.dto.SignupRequestDTO;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private AuthService authService;
  private ModelMapper modelMapper;

  @Autowired
  public AuthController(AuthService authService, ModelMapper modelMapper) {
    this.authService = authService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<User>> signup(@Valid @RequestBody SignupRequestDTO user){
    User createdUser = authService.register(modelMapper.map(user, User.class));
    ApiResponse<User> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(createdUser);
    response.setMessage("A new user as been created successfully");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO user){
    LoginResponseDTO loginResponse = authService.login(user);
    ApiResponse<LoginResponseDTO> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(loginResponse);
    response.setMessage("The user has login successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
