package com.github.pius.pichats.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.service.PostService;
import com.github.pius.pichats.service.Utils.PageResultConverter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping()
@CrossOrigin
public class PostController {
  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping(value = "/post")
  public ResponseEntity<ApiResponse<Object>> post(@Valid @RequestBody PostDTO post,
      @RequestParam("file") MultipartFile file, HttpServletRequest request) {
    Object newPost = postService.post(post, file, request);
    ApiResponse<Object> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newPost);
    response.setMessage("Post has been made successfully by a user");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/post/{post}")
  public ResponseEntity<ApiResponse<Post>> getAPost(@PathVariable(name = "post") String post,
      HttpServletRequest request) {
    Post postFound = postService.findPost(post, request);
    ApiResponse<Post> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(postFound);
    response.setMessage("Post have been retrieved");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{username}/post")
  public ResponseEntity<ApiResponse<PageResultConverter>> getAllPostsByUser(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "limit", defaultValue = "10") int limit, @PathVariable(name = "username") String username,
      HttpServletRequest request) {
    PageResultConverter posts = postService.findAllPostsByUser(page, limit, username, request);
    ApiResponse<PageResultConverter> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(posts);
    response.setMessage("All posts by user have been retrieved");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/post/all")
  public ResponseEntity<ApiResponse<PageResultConverter>> getAllPosts(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "limit", defaultValue = "10") int limit, HttpServletRequest request) {
    PageResultConverter posts = postService.findAll(page, limit, request);
    ApiResponse<PageResultConverter> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(posts);
    response.setMessage("All posts have been retrieved");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/post/{post}")
  public ResponseEntity<ApiResponse<String>> deleteAPost(@PathVariable(name = "post") String post,
      HttpServletRequest request) throws Exception {
    postService.delete(post, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage("Post have been deleted");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/post/{post}/select")
  public ResponseEntity<ApiResponse<String>> select(@PathVariable(name = "post") String post,
      HttpServletRequest request) {
    String message = postService.selectPostToDelete(post, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/post/select/clear")
  public ResponseEntity<ApiResponse<String>> clearSelectedPosts(HttpServletRequest request) {
    String message = postService.clearBatchDelete(request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @DeleteMapping("/post")
  public ResponseEntity<ApiResponse<String>> deleteSelectedPosts(HttpServletRequest request) throws Exception {
    String message = postService.batchDelete(request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/post/{username}/count")
  public ResponseEntity<ApiResponse<Integer>> countPosts(@PathVariable(name = "username") String username,
      HttpServletRequest request) {
    int numOfPost = postService.countPostsOfUser(username, request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfPost);
    response.setMessage("All posts have been counted");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
