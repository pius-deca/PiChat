package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.CommentDTO;
import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.service.CommentService;
import com.github.pius.pichats.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class CommentController {
  private CommentService commentService;
  private ModelMapper modelMapper;

  public CommentController(CommentService commentService, ModelMapper modelMapper) {
    this.commentService = commentService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/post/{postId}/comment")
  public ResponseEntity<ApiResponse<Comment>> comment(@PathVariable(name = "postId") Long postId, @Valid @RequestBody CommentDTO comment, HttpServletRequest request){
    Comment newComment = commentService.makeComment(postId, modelMapper.map(comment, Comment.class), request);
    ApiResponse<Comment> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newComment);
    response.setMessage("A post has been commented on successfully by a user");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

//  @GetMapping("/{post}")
//  public ResponseEntity<ApiResponse<Post>> getAPost(@PathVariable(name = "post") String post, HttpServletRequest request){
//    Post postFound = postService.findPost(post, request);
//    ApiResponse<Post> response = new ApiResponse<>(HttpStatus.OK);
//    response.setData(postFound);
//    response.setMessage("Post have been retrieved");
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }
//
//  @GetMapping()
//  public ResponseEntity<ApiResponse<List<Post>>> getAllPosts(HttpServletRequest request){
//    List<Post> posts = postService.findAll(request);
//    ApiResponse<List<Post>> response = new ApiResponse<>(HttpStatus.OK);
//    response.setData(posts);
//    response.setMessage("All posts have been retrieved");
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }

//  @DeleteMapping("/{post}")
//  public ResponseEntity<ApiResponse<String>> deleteAPost(@PathVariable(name = "post") String post, HttpServletRequest request) throws Exception {
//    postService.delete(post, request);
//    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
//    response.setMessage("Post have been deleted");
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }
}
