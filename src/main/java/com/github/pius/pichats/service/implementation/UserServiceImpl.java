package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.ChangePasswordDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private JwtProvider jwtProvider;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User searchByUsername(String username, HttpServletRequest request) {
    try{
      User user = jwtProvider.resolveUser(request);
      Optional<User> searchedUser = userRepository.findByUsername(username);
      if (searchedUser.isPresent()){
        if (!user.getUsername().equals(username)){
          return searchedUser.get();
        }
        throw new CustomException("Luckily!!! : '"+username+"' you just found yourself", HttpStatus.NOT_FOUND);
      }
      throw new CustomException("The user : '"+username+"' you are searching for does not exists", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request) {
    try{
      User user = jwtProvider.resolveUser(request);
      return this.newPassword(passwordDTO, user);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  private String newPassword(ChangePasswordDTO passwordDTO, User user) {
    if (passwordEncoder.matches(passwordDTO.getCurrentPassword(), user.getPassword())){
      if (passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())){
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
        return "The user has changed his/her password";
      }
      throw new CustomException("New Password not yet confirmed, password must match", HttpStatus.BAD_REQUEST);
    }
    throw new CustomException("Current password is not correct, please enter the correct password", HttpStatus.BAD_REQUEST);
  }

}
