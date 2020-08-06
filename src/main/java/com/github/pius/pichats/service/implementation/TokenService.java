package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.model.PasswordToken;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.PasswordTokenRepository;
import com.github.pius.pichats.service.Utils.Constants;
import com.github.pius.pichats.service.Utils.TokenGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TokenService {
  private final PasswordTokenRepository passwordTokenRepository;

  @Autowired
  public TokenService(PasswordTokenRepository passwordTokenRepository) {
    this.passwordTokenRepository = passwordTokenRepository;
  }

  public String activationToken(){
    return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
  }

  public PasswordToken generateTokenRecord(User user) {
    String token = TokenGenerator.generateCode();
    return savePasswordToken(token, user);
  }

  public PasswordToken generateTokenRecord(User user, int tokenLength) {
    String token = TokenGenerator.generateCode(tokenLength);
    return savePasswordToken(token, user);
  }

  private PasswordToken savePasswordToken(String token, User user) {
//    passwordTokenRepository.deleteByUser(user);

    PasswordToken pt = new PasswordToken();
    pt.setToken(token);
    pt.setUser(user);
    pt.setValidity(LocalDateTime.now().plusMinutes(Constants.RESET_PASSWORD_TOKEN_VALIDITY));
    return passwordTokenRepository.save(pt);
  }

//  private void expireToken(Token token) {
//    try{
//      passwordTokenRepository.expireToken(
//        LocalDateTime.now().minusMinutes(Constants.PHONE_NO_VERIFICATION_TOKEN_VALIDITY), token.getId());
//    } catch (Exception ex){
//      throw new CustomException("Something went wrong", HttpStatus.UNPROCESSABLE_ENTITY);
//    }
//  }
}
