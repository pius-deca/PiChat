package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.ForgotPassDTO;
import com.github.pius.pichats.dto.ResetPasswordDTO;
import com.github.pius.pichats.service.MapValidationErrorService;
import com.github.pius.pichats.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth/password")
@CrossOrigin
public class PasswordController {
  private final PasswordService passwordService;
  private final MapValidationErrorService mapValidationErrorService;

  @Autowired
  public PasswordController(PasswordService passwordService, MapValidationErrorService mapValidationErrorService) {
    this.passwordService = passwordService;
    this.mapValidationErrorService = mapValidationErrorService;
  }

  @PostMapping("/forgot")
  public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPassDTO forgotPassDTO,
      BindingResult bindingResult) {
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null) {
      return errorMap;
    }
    String msg = passwordService.forgotPassword(forgotPassDTO);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(msg);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/reset")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO,
      BindingResult bindingResult, @RequestParam("token") String token) {
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(bindingResult);
    if (errorMap != null) {
      return errorMap;
    }
    String msg = passwordService.resetPassword(resetPasswordDTO, token);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(msg);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
