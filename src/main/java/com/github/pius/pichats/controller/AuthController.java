package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.dto.SignupRequestDTO;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.service.AuthService;
import com.github.pius.pichats.service.MapValidationErrorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.validation.Valid;

@RestController
@RequestMapping()
@CrossOrigin
public class AuthController {
  private AuthService authService;
  private ModelMapper modelMapper;
  private MapValidationErrorService mapValidationErrorService;

  @Autowired
  public AuthController(AuthService authService, ModelMapper modelMapper, MapValidationErrorService mapValidationErrorService) {
    this.authService = authService;
    this.modelMapper = modelMapper;
    this.mapValidationErrorService = mapValidationErrorService;
  }

  @PostMapping("/auth/signup")
  public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDTO user, BindingResult bindingResult) throws MailSendException {
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null){
      return errorMap;
    }
    User createdUser = authService.register(modelMapper.map(user, User.class));
    ApiResponse<User> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(createdUser);
    response.setMessage("A new user as been created successfully");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/user/activate")
  public ResponseEntity<?> activate(@RequestParam String code){
    String activated = authService.activate(code);
    ApiResponse<User> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(activated);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO user, BindingResult bindingResult){
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null){
      return errorMap;
    }
    LoginResponseDTO loginResponse = authService.login(user);
    ApiResponse<LoginResponseDTO> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(loginResponse);
    response.setMessage("The user has login successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
