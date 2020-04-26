package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
  private PostService postService;
  private ModelMapper modelMapper;

  public PostController(PostService postService, ModelMapper modelMapper) {
    this.postService = postService;
    this.modelMapper = modelMapper;
  }

  @PostMapping()
  public ResponseEntity<ApiResponse<Post>> post(@Valid @RequestBody PostDTO post, HttpServletRequest request){
    Post newPost = postService.post(modelMapper.map(post, Post.class), request);
    ApiResponse<Post> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newPost);
    response.setMessage("A post has been made successfully by a user");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/{post}")
  public ResponseEntity<ApiResponse<Post>> getAPost(@PathVariable(name = "post") String post, HttpServletRequest request){
    Post postFound = postService.findPost(post, request);
    ApiResponse<Post> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(postFound);
    response.setMessage("Post have been retrieved");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping()
  public ResponseEntity<ApiResponse<List<Post>>> getAllPosts(HttpServletRequest request){
    List<Post> posts = postService.findAll(request);
    ApiResponse<List<Post>> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(posts);
    response.setMessage("All posts have been retrieved");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/{post}")
  public ResponseEntity<ApiResponse<String>> deleteAPost(@PathVariable(name = "post") String post, HttpServletRequest request) throws Exception {
    postService.delete(post, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Post have been deleted");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{post}/select")
  public ResponseEntity<ApiResponse<String>> select(@PathVariable(name = "post") String post, HttpServletRequest request){
    String message = postService.selectPostToDelete(post, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/select/clear")
  public ResponseEntity<ApiResponse<String>> clearSelectedPosts(HttpServletRequest request){
    String message = postService.clearBatchDelete(request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping()
  public ResponseEntity<ApiResponse<String>> deleteSelectedPosts(HttpServletRequest request) throws Exception {
    String message = postService.batchDelete(request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
