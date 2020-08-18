package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.dto.PostResponseDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Notification;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.NotificationRepository;
import com.github.pius.pichats.repository.PostRepository;
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
import java.util.Map;

@Service
public class PostServiceImpl implements PostService {
  private final JwtProvider jwtProvider;
  private final PostRepository postRepository;
  private final NotificationRepository notificationRepository;
  private final CloudService cloudService;
  private List<String> listOfPosts = new ArrayList<>();
  private final UserService userService;
  private final EntityPageIntoDtoPage entityPageIntoDtoPage;

  @Autowired
  public PostServiceImpl(JwtProvider jwtProvider, PostRepository postRepository,
      NotificationRepository notificationRepository, CloudService cloudService, UserService userService,
      EntityPageIntoDtoPage entityPageIntoDtoPage) {
    this.jwtProvider = jwtProvider;
    this.postRepository = postRepository;
    this.notificationRepository = notificationRepository;
    this.cloudService = cloudService;
    this.userService = userService;
    this.entityPageIntoDtoPage = entityPageIntoDtoPage;
  }

  // random post to comment on by any user
  @Override
  public Post getPost(String post) {
    try {
      return postRepository.findByPost(post)
          .orElseThrow(() -> new CustomException("Post : " + post + " does not exists", HttpStatus.NOT_FOUND));
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // a logged in user makes a post
  @Override
  public Object post(PostDTO caption, MultipartFile file, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      Post newPost = new Post();
      newPost.setCaption(caption.getCaption());
      // upload post if username exists
      Map uploaded = cloudService.upload(file);
      newPost.setPost(cloudService.getFileName());
      newPost.setUser(user);
      newPost.setUrl(uploaded.get("secure_url").toString());
      postRepository.save(newPost);
      // notify user once a post is created
      Notification notify = new Notification();
      notify.setActor(user.getUsername());
      notify.setMessage("You just created a post");
      notificationRepository.save(notify);
      return uploaded;
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // find a particular post of a logged in user
  @Override
  public Post findPost(String post, HttpServletRequest request) {
    try {
      jwtProvider.resolveUser(request);
      return this.getPost(post);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // return all the post of a logged in user
  @Override
  public PageResultConverter findAllPostsByUser(int page, int limit, String username, HttpServletRequest request) {
    try {
      User user = userService.searchByUsername(username, request);

      if (page > 0)
        page--;

      Pageable pageableReq = PageRequest.of(page, limit);

      Page<Post> entities = postRepository.findAllByUserOrderByCreatedAtDesc(pageableReq, user);

      Page<PostResponseDTO> postResponse = entityPageIntoDtoPage.mapEntityPageIntoDtoPage(entities,
          PostResponseDTO.class);

      postResponse.stream().forEach(res -> {
        res.setCaption(res.getCaption());
        res.setPost(res.getPost());
        res.setUrl(res.getUrl());
        res.setCreatedAt(res.getCreatedAt());
        res.setUpdatedAt(res.getUpdatedAt());
        res.setUser(res.getUser());
      });

      return new PageResultConverter(postResponse);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // return all the post of a logged in user
  @Override
  public PageResultConverter findAll(int page, int limit, HttpServletRequest request) {
    try {
      jwtProvider.resolveUser(request);

      if (page > 0)
        page--;

      Pageable pageableReq = PageRequest.of(page, limit);

      Page<Post> entities = postRepository.findAllByOrderByCreatedAtDesc(pageableReq);

      Page<PostResponseDTO> postResponse = entityPageIntoDtoPage.mapEntityPageIntoDtoPage(entities,
          PostResponseDTO.class);

      postResponse.stream().forEach(res -> {
        res.setCaption(res.getCaption());
        res.setPost(res.getPost());
        res.setUrl(res.getUrl());
        res.setCreatedAt(res.getCreatedAt());
        res.setUpdatedAt(res.getUpdatedAt());
        res.setNumOfLikes(res.getLikes().size());
        res.setNumOfComments(res.getComments().size());
        res.setUser(res.getUser());
      });

      return new PageResultConverter(postResponse);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // find a post of the logged in user and delete
  @Override
  public void delete(String post, HttpServletRequest request) throws Exception {
    User user = jwtProvider.resolveUser(request);
    // notify user once a post is deleted
    Notification notify = new Notification();
    notify.setActor(user.getUsername());
    notify.setMessage("You just deleted a post");
    notificationRepository.save(notify);
    cloudService.deleteFile(post);
    postRepository.delete(this.findPost(post, request));
  }

  // find a post of the logged in user mark put in a list
  @Override
  public String selectPostToDelete(String post, HttpServletRequest request) {

    Post postFound = this.findPost(post, request);
    if (!listOfPosts.contains(postFound.getPost())) {
      listOfPosts.add(postFound.getPost());
      return "Post : " + post + " have been marked";
    }
    listOfPosts.remove(postFound.getPost());
    return "Post : " + post + " have been unmarked";
  }

  // clear the list of posts if not empty
  @Override
  public String clearBatchDelete(HttpServletRequest request) {

    if (!listOfPosts.isEmpty()) {
      listOfPosts.clear();
      return "Post(s) marked for delete have been canceled";
    }
    return "Post(s) marked for delete is empty";
  }

  // delete the list posts of a logged in user
  @Override
  public String batchDelete(HttpServletRequest request) throws Exception {

    if (!listOfPosts.isEmpty()) {
      for (String post : listOfPosts) {
        delete(post, request);
      }
      listOfPosts.clear();
      return "Posts marked have been deleted";
    }
    return "There is no post(s) marked to delete";
  }

  @Override
  public int countPostsOfUser(String username, HttpServletRequest request) {
    try {
      User user = userService.searchByUsername(username, request);
      return postRepository.countPostsByUser(user);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

}
