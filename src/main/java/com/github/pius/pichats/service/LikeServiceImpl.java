package com.github.pius.pichats.service;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.LikeRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class LikeServiceImpl implements LikeService {
  private LikeRepository likeRepository;
  private UserRepository userRepository;
  private JwtProvider jwtProvider;
  private PostService postService;

  @Autowired
  public LikeServiceImpl(LikeRepository likeRepository, UserRepository userRepository, JwtProvider jwtProvider, PostService postService) {
    this.likeRepository = likeRepository;
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.postService = postService;
  }

  // this method enables a user likes or unlikes a post made by any user
  @Override
  public Like likeOrUnlike(String post, HttpServletRequest request) {
    try{
      User user = jwtProvider.resolveUser(request);
      // find any post to like
      Post postFound = postService.getPost(post);
      Like newLike = new Like();
      Optional<Like> foundLike = likeRepository.findByPost(postFound);
      return saveLike(user, postFound, newLike, foundLike);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method likes or unlikes
  private Like saveLike(User user, Post postFound, Like newLike, Optional<Like> foundLike) {
    if (foundLike.isPresent()){
      if (foundLike.get().isLikes()){
        foundLike.get().setLikes(false);
        return likeRepository.save(foundLike.get());
      }
      foundLike.get().setLikes(true);
      return likeRepository.save(foundLike.get());
    }
    newLike.setUser(user);
    newLike.setPost(postFound);
    newLike.setLikes(true);
    return likeRepository.save(newLike);
  }

  @Override
  public int countPostLikes(String post, HttpServletRequest request) {
    Post postFound = postService.findPost(post, request);
    Optional<Like> like = likeRepository.findByPost(postFound);
    int numOfLikes = 0;
    if (like.isPresent()){
      if (like.get().isLikes()){
        numOfLikes = likeRepository.countLikesByPost(postFound);
      }
    }
    return numOfLikes;
  }
}
