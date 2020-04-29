package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.ChangePasswordDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.FollowRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private FollowRepository followRepository;
  private JwtProvider jwtProvider;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, FollowRepository followRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.followRepository = followRepository;
    this.jwtProvider = jwtProvider;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User searchByUsername(String username, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      Optional<User> searchedUser = userRepository.findByUsername(username);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          if (searchedUser.isPresent()){
            if (!userByUsername.get().getUsername().equals(username)){
              return searchedUser.get();
            }
            throw new CustomException("Luckily!!! : '"+username+"' you just found yourself", HttpStatus.NOT_FOUND);
          }
          throw new CustomException("The user : '"+username+"' you are searching for does not exists", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        if (searchedUser.isPresent()){
          if (!userByEmail.get().getUsername().equals(username)){
            return searchedUser.get();
          }
          throw new CustomException("Luckily!!! :'"+username+"' you just found yourself", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("The user : '"+username+"' you are searching for does not exists", HttpStatus.NOT_FOUND);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
         return newPassword(passwordDTO, userByUsername);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        return newPassword(passwordDTO, userByEmail);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  private String newPassword(ChangePasswordDTO passwordDTO, Optional<User> identifier) {
    if (passwordEncoder.matches(passwordDTO.getCurrentPassword(), identifier.get().getPassword())){
      if (passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())){
        identifier.get().setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(identifier.get());
        return "The user has changed his/her password";
      }
      throw new CustomException("New Password not yet confirmed, password must match", HttpStatus.BAD_REQUEST);
    }
    throw new CustomException("Current password is not correct, please enter the correct password", HttpStatus.BAD_REQUEST);
  }


}
