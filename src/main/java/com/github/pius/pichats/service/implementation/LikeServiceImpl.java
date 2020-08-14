package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.model.Notification;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.LikeRepository;
import com.github.pius.pichats.repository.NotificationRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.LikeService;
import com.github.pius.pichats.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {
  private final LikeRepository likeRepository;
  private final NotificationRepository notificationRepository;
  private final JwtProvider jwtProvider;
  private final PostService postService;

  @Autowired
  public LikeServiceImpl(LikeRepository likeRepository, NotificationRepository notificationRepository,
      JwtProvider jwtProvider, PostService postService) {
    this.likeRepository = likeRepository;
    this.notificationRepository = notificationRepository;
    this.jwtProvider = jwtProvider;
    this.postService = postService;
  }

  // this method enables a user likes or unlikes a post made by any user
  @Override
  public Like likeOrUnlike(String post, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      if (user.isActive()) {
        // find any post to like
        Post postFound = postService.getPost(post);
        Like newLike = new Like();
        Optional<Like> foundLike = likeRepository.findByPost(postFound);
        return this.saveLike(user, postFound, newLike, foundLike);
      }
      throw new CustomException("Your account is yet to be activated", HttpStatus.UNAUTHORIZED);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method likes or unlikes
  private Like saveLike(User user, Post postFound, Like newLike, Optional<Like> foundLike) {
    if (foundLike.isPresent()) {
      likeRepository.delete(foundLike.get());
      return foundLike.get();
    }
    Notification notify = new Notification();
    newLike.setUser(user);
    newLike.setPost(postFound);

    // notify user that his/her post was liked
    notify.setActor(user.getUsername());
    notify.setMessage(user.getUsername() + " liked your post");
    notify.setSubject(postFound.getUser());
    notificationRepository.save(notify);
    return likeRepository.save(newLike);

  }

  @Override
  public int countPostLikes(String post, HttpServletRequest request) {
    Post postFound = postService.findPost(post, request);
    Optional<Like> like = likeRepository.findByPost(postFound);
    int numOfLikes = 0;
    if (like.isPresent()) {
      numOfLikes = likeRepository.countLikesByPost(postFound);
    }
    return numOfLikes;
  }
}
