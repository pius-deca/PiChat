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
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);

      // find any post to like
      Post postFound = postService.getPost(post);
      Like newLike = new Like();
      Optional<Like> foundLike = likeRepository.findByPost(postFound);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          return saveLike(userByUsername, postFound, newLike, foundLike);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        return saveLike(userByEmail, postFound, newLike, foundLike);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method likes or unlikes
  private Like saveLike(Optional<User> identifier, Post postFound, Like newLike, Optional<Like> foundLike) {
    if (foundLike.isPresent()){
      if (foundLike.get().isLikes()){
        foundLike.get().setLikes(false);
        return likeRepository.save(foundLike.get());
      }
      foundLike.get().setLikes(true);
      return likeRepository.save(foundLike.get());
    }
    newLike.setUser(identifier.get());
    newLike.setPost(postFound);
    newLike.setLikes(true);
    return likeRepository.save(newLike);
  }
}
