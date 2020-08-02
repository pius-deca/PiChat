package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.ResetPasswordDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.PasswordToken;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.PasswordTokenRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.service.EmailSenderService;
import com.github.pius.pichats.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordServiceImpl implements PasswordService {
  private final UserRepository userRepository;
  private final EmailSenderService emailSenderService;
  private final PasswordTokenRepository passwordTokenRepository;
  private final TokenService tokenService;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public PasswordServiceImpl(UserRepository userRepository, EmailSenderService emailSenderService, PasswordTokenRepository passwordTokenRepository, TokenService tokenService, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.emailSenderService = emailSenderService;
    this.passwordTokenRepository = passwordTokenRepository;
    this.tokenService = tokenService;
    this.passwordEncoder = passwordEncoder;
  }

  public String forgotPassword(String identifier){
//    try {
      Optional<User> userByEmail = userRepository.findByEmail(identifier.toLowerCase());
      Optional<User> userByUsername = userRepository.findByUsername(identifier.toLowerCase());
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          PasswordToken token = tokenService.generateTokenRecord(userByUsername.get());
          this.emailSenderService.sendMail(userByUsername.get().getEmail(), "Reset pichat password", "Hi "+userByUsername.get().getUsername()+"\n"+"A request was made to reset your pichat password. Click the link below to reset password"+ "\n"+"http://localhost:3000/account/password/reset?token="+token.getToken());
          return "Thanks! Please check your email for a link to reset your password.";
        }
        throw new CustomException("Email or Username doesn't exists", HttpStatus.BAD_REQUEST);
      }
      PasswordToken token = tokenService.generateTokenRecord(userByEmail.get());
      this.emailSenderService.sendMail(userByEmail.get().getEmail(), "Reset pichat password", "Hi "+userByEmail.get().getUsername()+"\n"+"A request was made to reset your pichat password"+ "\n"+"http://localhost:3000/account/password/reset?token="+token.getToken());
      return "Thanks! Please check your email for a link to reset your password.";
//    }catch (Exception e){
//      throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
  }

  public void resetPassword(ResetPasswordDTO resetPasswordDTO, String token){
    String password = resetPasswordDTO.getPassword();
    String confirmPassword = resetPasswordDTO.getConfirmPassword();

    Optional<PasswordToken> pt = passwordTokenRepository.findByToken(token);
    if (!pt.isPresent()){
      throw new CustomException("Token doesn't exists", HttpStatus.BAD_REQUEST);
    }

    boolean expired = pt.get().getValidity().isBefore(LocalDateTime.now());

    if (expired){
      throw new CustomException("Token has expired", HttpStatus.BAD_REQUEST);
    }

    User user = pt.get().getUser();
    if (!password.equals(confirmPassword)){
      throw new CustomException("New Password not yet confirmed, passwords must match", HttpStatus.BAD_REQUEST);
    }
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
  }

}
