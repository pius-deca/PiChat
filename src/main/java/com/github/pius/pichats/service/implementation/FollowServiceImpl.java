package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.FollowRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.UserService;
import com.github.pius.pichats.service.FollowService;
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
  private AuthServiceImpl authServiceImpl;

  @Autowired
  public FollowServiceImpl(FollowRepository followRepository, UserService userService, JwtProvider jwtProvider, AuthServiceImpl authServiceImpl, UserRepository userRepository) {
    this.followRepository = followRepository;
    this.userService = userService;
    this.jwtProvider = jwtProvider;
    this.authServiceImpl = authServiceImpl;
    this.userRepository = userRepository;
  }

  @Override
  public Follow follow(String username, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = jwtProvider.resolveUser(request);
      User foundUserToFollow = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowing(username);
      Follow follow = new Follow();
      if (!following.isPresent()) {
        follow.setFollowing(foundUserToFollow.getUsername());
        follow.setUser(user);
        return followRepository.save(follow);
      }
      if (following.get().isAccepted()) {
        throw new CustomException("User '" + username + "' has already been followed", HttpStatus.NOT_FOUND);
      } else {
        throw new CustomException("User '" + user.getUsername() + "' has sent a request to follow '" + username + "'", HttpStatus.NOT_FOUND);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public Follow acceptRequest(String username, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User foundRequester = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByUser(foundRequester);
      if (following.isPresent()) {
        following.get().setAccepted(true);
        return followRepository.save(following.get());
      }
      throw new CustomException("User '" + username + "' did not make any request to follow you", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String declineRequest(String username, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = jwtProvider.resolveUser(request);
      User foundRequester = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByUser(foundRequester);
      if (following.isPresent()){
        followRepository.delete(following.get());
        return "User '"+user.getUsername()+"' declines '"+username+"' request";
      }
      throw new CustomException("User '"+username+"' did not make any request to follow you", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String unFollow(String username, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = jwtProvider.resolveUser(request);
      userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowing(username);
      if(following.isPresent()){
        followRepository.delete(following.get());
        return "User '"+user.getUsername()+"' has unfollowed '"+username+"' successfully";
      }
      throw new CustomException("User '"+user.getUsername()+"' has already unfollowed '"+username+"'", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

//  @Override
//  public int countFollowers(HttpServletRequest request){
//    try{
////      authServiceImpl.isAccountActive(request);
//      User user = jwtProvider.resolveUser(request);
//      return followRepository.countFollowersByFollowingAndAccepted(user.getUsername(), true);
//    }catch (Exception ex){
//      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//  }
//
//  @Override
//  public int countFollowing(HttpServletRequest request){
//    try{
////      authServiceImpl.isAccountActive(request);
//      User user = jwtProvider.resolveUser(request);
//      return followRepository.countFollowingByUserAndAccepted(user, true);
//    }catch (Exception ex){
//      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
//    }
//  }

  @Override
  public int countFollowersOfSearchedUser(String username, HttpServletRequest request){
    try{
//      authServiceImpl.isAccountActive(request);
      User searchedUser = userService.searchByUsername(username, request);
      return followRepository.countFollowersByFollowingAndAccepted(searchedUser.getUsername(), true);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public int countFollowingSearchedUser(String username, HttpServletRequest request){
    try{
//      authServiceImpl.isAccountActive(request);
      User searchedUser = userService.searchByUsername(username, request);
      return followRepository.countFollowingByUserAndAccepted(searchedUser, true);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
