package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.ResetPasswordDTO;
import com.github.pius.pichats.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/password")
@CrossOrigin
public class PasswordController {
  private final PasswordService passwordService;

  @Autowired
  public PasswordController(PasswordService passwordService) {
    this.passwordService = passwordService;
  }

  @PostMapping("/forgot")
  public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam("identifier") String identifier){
    String msg = passwordService.forgotPassword(identifier);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(msg);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/reset")
  public ResponseEntity<ApiResponse<String>> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO, @RequestParam("token") String token){
    passwordService.resetPassword(resetPasswordDTO, token);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Password has been changed, please log in");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
