package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.AuthResponseDTO;
import com.github.pius.pichats.dto.SignupRequestDTO;
import com.github.pius.pichats.service.AuthService;
import com.github.pius.pichats.service.MapValidationErrorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping()
@CrossOrigin
public class AuthController {
  private final AuthService authService;
  private final MapValidationErrorService mapValidationErrorService;

  @Autowired
  public AuthController(AuthService authService, ModelMapper modelMapper,
      MapValidationErrorService mapValidationErrorService) {
    this.authService = authService;
    this.mapValidationErrorService = mapValidationErrorService;
  }

  @PostMapping("/auth/signup")
  public ResponseEntity<?> signUp(@Valid @RequestBody SignupRequestDTO user, BindingResult bindingResult)
      throws Exception {
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null) {
      return errorMap;
    }
    AuthResponseDTO createdUser = authService.register(user);
    ApiResponse<AuthResponseDTO> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(createdUser);
    response.setMessage("Signup successful, go to email and get activation code");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/user/activate")
  public ResponseEntity<ApiResponse<String>> activate(@RequestParam("code") String code, HttpServletRequest request) {
    String activated = authService.activate(code, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(activated);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO user, BindingResult bindingResult) {
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null) {
      return errorMap;
    }
    AuthResponseDTO loginResponse = authService.login(user);
    ApiResponse<AuthResponseDTO> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(loginResponse);
    response.setMessage("User has login successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
