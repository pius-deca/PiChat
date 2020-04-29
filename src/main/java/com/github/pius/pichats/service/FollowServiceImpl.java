package com.github.pius.pichats.service;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.FollowRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class FollowServiceImpl implements FollowService {
  private FollowRepository followRepository;
  private UserRepository userRepository;
  private UserService userService;
  private JwtProvider jwtProvider;

  @Autowired
  public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository, UserService userService, JwtProvider jwtProvider) {
    this.followRepository = followRepository;
    this.userRepository = userRepository;
    this.userService = userService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Follow follow(String username, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      User foundUserToFollow = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowing(username);
      Follow follow = new Follow();
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          if (!following.isPresent()){
            follow.setFollowing(foundUserToFollow.getUsername());
            follow.setUser(userByUsername.get());
            return followRepository.save(follow);
          }
          throw new CustomException("User '"+username+"' has already been followed", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        if (!following.isPresent()){
          follow.setFollowing(foundUserToFollow.getUsername());
          follow.setUser(userByEmail.get());
          return followRepository.save(follow);
        }
        throw new CustomException("User '"+username+"' has already been followed", HttpStatus.NOT_FOUND);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String unFollow(String username, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowing(username);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          if(following.isPresent()){
            followRepository.delete(following.get());
            return "User has unfollowed '"+username+"' successfully";
          }
          throw new CustomException("User '"+username+"' has already been unfollowed", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        if(following.isPresent()){
          followRepository.delete(following.get());
          return "User has unfollowed '"+username+"' successfully";
        }
        throw new CustomException("User '"+username+"' has already been unfollowed", HttpStatus.NOT_FOUND);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public int countFollowers(HttpServletRequest request){
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          return followRepository.countFollowersByFollowing(identifier);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        return followRepository.countFollowersByFollowing(userByEmail.get().getUsername());
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public int countFollowing(HttpServletRequest request){
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          return followRepository.countFollowingByUser(userByUsername.get());
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        return followRepository.countFollowingByUser(userByEmail.get());
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
