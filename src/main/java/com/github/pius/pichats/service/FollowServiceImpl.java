package com.github.pius.pichats.service;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.FollowRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class FollowServiceImpl implements FollowService {
  private FollowRepository followRepository;
  private UserService userService;
  private JwtProvider jwtProvider;

  @Autowired
  public FollowServiceImpl(FollowRepository followRepository, UserService userService, JwtProvider jwtProvider) {
    this.followRepository = followRepository;
    this.userService = userService;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Follow follow(String username, HttpServletRequest request) {
    try{
      User user = jwtProvider.resolveUser(request);
      User foundUserToFollow = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowing(username);
      Follow follow = new Follow();
      if (!following.isPresent()){
        follow.setFollowing(foundUserToFollow.getUsername());
        follow.setUser(user);
        return followRepository.save(follow);
      }
      throw new CustomException("User '"+username+"' has already been followed", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String unFollow(String username, HttpServletRequest request) {
    try{
      jwtProvider.resolveUser(request);
      userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowing(username);
      if(following.isPresent()){
        followRepository.delete(following.get());
        return "User has unfollowed '"+username+"' successfully";
      }
      throw new CustomException("User '"+username+"' has already been unfollowed", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public int countFollowers(HttpServletRequest request){
    try{
      User user = jwtProvider.resolveUser(request);
      return followRepository.countFollowersByFollowing(user.getUsername());
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public int countFollowing(HttpServletRequest request){
    try{
      User user = jwtProvider.resolveUser(request);
      return followRepository.countFollowingByUser(user);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
