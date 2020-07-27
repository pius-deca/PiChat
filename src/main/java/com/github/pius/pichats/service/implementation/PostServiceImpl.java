package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.dto.PostResponseDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.PostRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.PostService;
import com.github.pius.pichats.service.UserService;
import com.github.pius.pichats.service.Utils.EntityPageIntoDtoPage;
import com.github.pius.pichats.service.Utils.PageResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {
  private JwtProvider jwtProvider;
  private PostRepository postRepository;
  private UserRepository userRepository;
  private CloudService cloudService;
  private List<String> listOfPosts = new ArrayList<>();
  private AuthServiceImpl authServiceImpl;
  private UserService userService;
  private final EntityPageIntoDtoPage entityPageIntoDtoPage;

  @Autowired
  public PostServiceImpl(JwtProvider jwtProvider, PostRepository postRepository, CloudService cloudService, AuthServiceImpl authServiceImpl, UserService userService, UserRepository userRepository, EntityPageIntoDtoPage entityPageIntoDtoPage) {
    this.jwtProvider = jwtProvider;
    this.postRepository = postRepository;
    this.cloudService = cloudService;
    this.authServiceImpl = authServiceImpl;
    this.userService = userService;
    this.userRepository = userRepository;
    this.entityPageIntoDtoPage = entityPageIntoDtoPage;
  }

  // random post to comment on by any user
  @Override
  public Post getPost(String post) {
    try{
      return postRepository.findByPost(post).orElseThrow(() -> new CustomException("Post : "+post+ " does not exists", HttpStatus.NOT_FOUND));
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // a logged in user makes a post
  @Override
  public Object post(PostDTO post, MultipartFile file, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = jwtProvider.resolveUser(request);
      Post newPost = new Post();
      newPost.setCaption(post.getCaption());
      // upload post if username exists
      Object uploaded = cloudService.upload(file);
      newPost.setPost(cloudService.getFileName());
      newPost.setUser(user);
      newPost.setUrl(uploaded.toString());
      postRepository.save(newPost);
      return uploaded;
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // find a particular post of a logged in user
  @Override
  public Post findPost(String post, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = jwtProvider.resolveUser(request);
      Post postFound = this.getPost(post);
      if (postFound.getUser().equals(user)) {
        return postFound;
      }
      throw new CustomException(user.getUsername() + " does not have post " + post, HttpStatus.NOT_FOUND);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // return all the post of a logged in user
  @Override
  public PageResultConverter findAllPostsByUser(int page, int limit, String username, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = userService.searchByUsername(username, request);

      if(page > 0) page--;

      Pageable pageableReq = PageRequest.of(page, limit);

      Page<Post> entities = postRepository.findAllByUserOrderByCreatedAtDesc(pageableReq, user);

      Page<PostResponseDTO> postResponse = entityPageIntoDtoPage.mapEntityPageIntoDtoPage(entities, PostResponseDTO.class);

      postResponse.stream().forEach(res -> {
        res.setCaption(res.getCaption());
        res.setPost(res.getPost());
        res.setUrl(res.getUrl());
        res.setUser(res.getUser());
      });

      return new PageResultConverter(postResponse);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // return all the post of a logged in user
  @Override
  public PageResultConverter findAll(int page, int limit, HttpServletRequest request) {
    try{
      jwtProvider.resolveUser(request);

      if(page > 0) page--;

      Pageable pageableReq = PageRequest.of(page, limit);

      Page<Post> entities = postRepository.findAllByOrderByCreatedAtDesc(pageableReq);

      Page<PostResponseDTO> postResponse = entityPageIntoDtoPage.mapEntityPageIntoDtoPage(entities, PostResponseDTO.class);

      postResponse.stream().forEach(res -> {
        res.setCaption(res.getCaption());
        res.setPost(res.getPost());
        res.setUrl(res.getUrl());
        res.setUser(res.getUser());
      });

      return new PageResultConverter(postResponse);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // find a post of the logged in user and delete
  @Override
  public void delete(String post, HttpServletRequest request) throws Exception {
//    authServiceImpl.isAccountActive(request);
    cloudService.deleteFile(post);
    postRepository.delete(this.findPost(post, request));
  }

  // find a post of the logged in user mark put in a list
  @Override
  public String selectPostToDelete(String post, HttpServletRequest request){
//    authServiceImpl.isAccountActive(request);
    Post postFound = this.findPost(post, request);
    if (!listOfPosts.contains(postFound.getPost())){
      listOfPosts.add(postFound.getPost());
      return "Post : "+post+" have been marked";
    }
    listOfPosts.remove(postFound.getPost());
    return "Post : "+post+" have been unmarked";
  }

  // clear the list of posts if not empty
  @Override
  public String clearBatchDelete(HttpServletRequest request){
//    authServiceImpl.isAccountActive(request);
    if (!listOfPosts.isEmpty()){
      listOfPosts.clear();
      return "Post(s) marked for delete have been canceled";
    }
    return "Post(s) marked for delete is empty";
  }

  // delete the list posts of a logged in user
  @Override
  public String batchDelete(HttpServletRequest request) throws Exception {
//    authServiceImpl.isAccountActive(request);
    if (!listOfPosts.isEmpty()){
      for (String post : listOfPosts){
        delete(post, request);
      }
      listOfPosts.clear();
      return "Posts marked have been deleted";
    }
    return "There is no post(s) marked to delete";
  }

  @Override
  public int countPostsOfUser(String username, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = userService.searchByUsername(username, request);
      return postRepository.countPostsByUser(user);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

}
