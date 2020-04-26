package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.SearchUsernameDto;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
  private UserRepository userRepository;
  private JwtProvider jwtProvider;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider) {
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public User searchByUsername(SearchUsernameDto username, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      Optional<User> searchedUser = userRepository.findByUsername(username.getUsername());
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          if (searchedUser.isPresent()){
            if (!userByUsername.get().getUsername().equals(username.getUsername())){
              return searchedUser.get();
            }
            throw new CustomException("Luckily!!! : '"+username.getUsername()+"' you just found yourself", HttpStatus.NOT_FOUND);
          }
          throw new CustomException("The user : '"+username.getUsername()+"' you are searching for does not exists", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        if (searchedUser.isPresent()){
          if (!userByEmail.get().getUsername().equals(username.getUsername())){
            return searchedUser.get();
          }
          throw new CustomException("Luckily!!! :'"+username.getUsername()+"' you just found yourself", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("The user : '"+username.getUsername()+"' you are searching for does not exists", HttpStatus.NOT_FOUND);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
