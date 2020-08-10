package com.github.pius.pichats.service.implementation;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.FollowRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.FollowService;
import com.github.pius.pichats.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {
  private final FollowRepository followRepository;
  private final UserRepository userRepository;
  private final UserService userService;
  private final JwtProvider jwtProvider;

  @Autowired
  public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository, UserService userService,
      JwtProvider jwtProvider) {
    this.followRepository = followRepository;
    this.userService = userService;
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
  }

  @Override
  public Follow follow(String username, HttpServletRequest request) {
    try {
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
        throw new CustomException("User '" + user.getUsername() + "' has sent a request to follow '" + username + "'",
            HttpStatus.NOT_FOUND);
      }
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public Follow acceptRequest(String username, HttpServletRequest request) {
    try {
      User foundRequester = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByUser(foundRequester);
      if (following.isPresent()) {
        following.get().setAccepted(true);
        return followRepository.save(following.get());
      }
      throw new CustomException("User '" + username + "' did not make any request to follow you", HttpStatus.NOT_FOUND);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String declineRequest(String username, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      User foundRequester = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByUser(foundRequester);
      if (following.isPresent()) {
        followRepository.delete(following.get());
        return "User '" + user.getUsername() + "' declines '" + username + "' request";
      }
      throw new CustomException("User '" + username + "' did not make any request to follow you", HttpStatus.NOT_FOUND);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String unFollow(String username, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowing(username);
      if (following.isPresent()) {
        followRepository.delete(following.get());
        return "User '" + user.getUsername() + "' has unfollowed '" + username + "' successfully";
      }
      throw new CustomException("User '" + user.getUsername() + "' has already unfollowed '" + username + "'",
          HttpStatus.NOT_FOUND);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public int countFollowersOfSearchedUser(String username, HttpServletRequest request) {
    try {
      User searchedUser = userService.searchByUsername(username, request);
      return followRepository.countFollowersByFollowingAndAccepted(searchedUser.getUsername(), true);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public int countFollowingSearchedUser(String username, HttpServletRequest request) {
    try {
      User searchedUser = userService.searchByUsername(username, request);
      return followRepository.countFollowingByUserAndAccepted(searchedUser, true);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public List<Follow> listOfFollowers(String username, HttpServletRequest request) {
    try {
      jwtProvider.resolveUser(request);
      return followRepository.findAllByFollowingAndAccepted(username, true);
    } catch (Exception e) {
      throw new CustomException(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public List<Follow> listOfFollowing(String username, HttpServletRequest request) {
    try {
      jwtProvider.resolveUser(request);
      Optional<User> user = userRepository.findByUsername(username);
      if (user.isPresent()) {
        return followRepository.findAllByUserAndAccepted(user.get(), true);
      }
      throw new CustomException("User : " + username + " does not exists", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      throw new CustomException(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
