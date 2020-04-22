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
  private CloudConfiguration cloudConfiguration;
  String fileName = null;

  @Autowired
  public PostServiceImpl(JwtProvider jwtProvider, UserRepository userRepository, PostRepository postRepository, CloudConfiguration cloudConfiguration) {
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
    this.cloudConfiguration = cloudConfiguration;
  }

//  protected String extension(String path){
//    return path.substring(path.length() - 3);
//  }

  protected void upload(String path) throws Exception {
    try {
      String num = RandomStringUtils.randomNumeric(5);
      File toUpload = new File(path);
      String folder = "piChat";
      String resource_type = null;
      if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".pdf")){
        fileName = "image" + num;
        folder = folder+"/image/";
        resource_type = "image";
      }else if(path.endsWith(".mp4") || path.endsWith(".avi")){
        fileName = "video" + num;
        folder = folder+"/video/";
        resource_type = "video";
      }
      Map params = ObjectUtils.asMap("public_id", folder + fileName,
        "resource_type", resource_type);
      cloudConfiguration.configCloud().uploader().upload(toUpload, params);
    }catch (IOException ex){
      throw new Exception(ex.getMessage());
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
          upload(post.getPost());
          newPost.setPost(fileName);
          newPost.setUser(userByUsername.get());
          return postRepository.save(newPost);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        Post newPost = new Post();
        newPost.setCaption(post.getCaption());
        // upload post if email exists
        upload(post.getPost());
        newPost.setPost(fileName);
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
      Optional<Post> postFound = postRepository.findByPost(post);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          if (postFound.isPresent()){
            if (postFound.get().getUser().equals(userByUsername.get())){
              return postFound.get();
            }
            throw new CustomException(identifier+ " does not have project "+post, HttpStatus.NOT_FOUND);
          }
          throw new CustomException(post+ " does not exists", HttpStatus.NOT_FOUND);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }
      if (postFound.isPresent()){
        if (postFound.get().getUser().equals(userByEmail.get())){
          return postFound.get();
        }
        throw new CustomException(identifier+ " does not have project "+post, HttpStatus.NOT_FOUND);
      }
      throw new CustomException(post+ " does not exists", HttpStatus.NOT_FOUND);
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
}
