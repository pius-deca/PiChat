package com.github.pius.pichats.service.implementation;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.Notification;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.FollowRepository;
import com.github.pius.pichats.repository.NotificationRepository;
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
  private final NotificationRepository notificationRepository;
  private final UserService userService;
  private final JwtProvider jwtProvider;

  @Autowired
  public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository,
      NotificationRepository notificationRepository, UserService userService, JwtProvider jwtProvider) {
    this.followRepository = followRepository;
    this.userService = userService;
    this.notificationRepository = notificationRepository;
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
  }

  @Override
  public Follow follow(String username, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      User foundUserToFollow = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowingAndUser(foundUserToFollow.getUsername(), user);
      Follow follow = new Follow();
      Notification notify = new Notification();
      if (!following.isPresent()) {
        follow.setFollowing(foundUserToFollow.getUsername());
        if (foundUserToFollow.getProfilePic() != null) {
          follow.setProfilePic(foundUserToFollow.getProfilePic().getUrl());
        } else {
          follow.setProfilePic(null);
        }
        follow.setFollowingBack("requested");
        follow.setUser(user);

        // notify user of a request to follow
        notify.setActor(user.getUsername());
        notify.setMessage(user.getUsername() + " wants to follow you");
        notify.setSubject(foundUserToFollow);
        notificationRepository.save(notify);
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
      User user = jwtProvider.resolveUser(request);
      User foundRequester = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowingAndUser(user.getUsername(), foundRequester);
      Optional<Follow> follower = followRepository.findByFollowingAndUser(foundRequester.getUsername(), user);
      // Optional<Follow> following = followRepository.findByFollowing(username);
      Notification notify = new Notification();
      if (following.isPresent()) {
        following.get().setAccepted(true);
        if (follower.isPresent()) {
          following.get().setFollowingBack("true");
          follower.get().setFollowingBack("true");
        }

        // notify user that request has been accepted
        notify.setActor(user.getUsername());
        notify.setMessage(user.getUsername() + " accepted request to follow");
        notify.setSubject(foundRequester);
        notificationRepository.save(notify);
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
      Optional<Follow> following = followRepository.findByFollowingAndUser(user.getUsername(), foundRequester);
      Notification notify = new Notification();
      if (following.isPresent()) {
        followRepository.delete(following.get());

        // notify user that request has been decline
        notify.setActor(user.getUsername());
        notify.setMessage(user.getUsername() + " declined request to follow");
        notify.setSubject(foundRequester);
        notificationRepository.save(notify);
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
      User foundUserToUnFollow = userService.searchByUsername(username, request);
      Optional<Follow> following = followRepository.findByFollowingAndUser(foundUserToUnFollow.getUsername(), user);
      Notification notify = new Notification();
      if (following.isPresent()) {
        followRepository.delete(following.get());

        // notify user that he/her has unfollwed an account
        notify.setActor(user.getUsername());
        notify.setMessage(user.getUsername() + " unfollowed you");
        notify.setSubject(foundUserToUnFollow);
        notificationRepository.save(notify);
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

  @Override
  public String isFollowing(String username, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      Optional<Follow> following = followRepository.findByFollowingAndUser(username, user);
      if (following.isPresent() && following.get().isAccepted()) {
        return "Yes";
      } else if (following.isPresent() && !following.get().isAccepted()) {
        return "Requested";
      }
      return "No";
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
