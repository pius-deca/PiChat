package com.github.pius.pichats.service;

import com.cloudinary.utils.ObjectUtils;
import com.github.pius.pichats.configuration.CloudConfiguration;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.PostRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
  private JwtProvider jwtProvider;
  private UserRepository userRepository;
  private PostRepository postRepository;
  private CloudService cloudService;

  @Autowired
  public PostServiceImpl(JwtProvider jwtProvider, UserRepository userRepository, PostRepository postRepository, CloudService cloudService) {
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.cloudService = cloudService;
  }

  // random post to comment on by any user
  @Override
  public Post getPost(String post) {
    try{
      Optional<Post> postFound = postRepository.findByPost(post);
      if (postFound.isPresent()){
        return postFound.get();
      }
      throw new CustomException("Post : "+post+ " does not exists", HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public Post post(Post post, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          Post newPost = new Post();
          newPost.setCaption(post.getCaption());
          // upload post if username exists
          cloudService.upload(post.getPost());
          newPost.setPost(cloudService.fileName);
          newPost.setUser(userByUsername.get());
          return postRepository.save(newPost);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        Post newPost = new Post();
        newPost.setCaption(post.getCaption());
        // upload post if email exists
        cloudService.upload(post.getPost());
        newPost.setPost(cloudService.fileName);
        newPost.setUser(userByEmail.get());
        return postRepository.save(newPost);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public Post findPost(String post, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      Post postFound = getPost(post);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          if (postFound.getUser().equals(userByUsername.get())){
            return postFound;
          }
          throw new CustomException(identifier+ " does not have project "+post, HttpStatus.NOT_FOUND);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }
      if (postFound.getUser().equals(userByEmail.get())){
        return postFound;
      }
      throw new CustomException(identifier+ " does not have project "+post, HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public List<Post> findAll(HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          return postRepository.findAllByUser(userByUsername.get());
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        return postRepository.findAllByUser(userByEmail.get());
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public void delete(String post, HttpServletRequest request) throws Exception {
    cloudService.deleteFile(post);
    postRepository.delete(findPost(post, request));
  }

  @Override
  public void deleteAll(HttpServletRequest request) {
    postRepository.deleteAll(findAll(request));
  }
}
