package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping
public class LikeController {
  private LikeService likeService;

  public LikeController(LikeService likeService) {
    this.likeService = likeService;
  }

  @PostMapping("/post/{postId}/like")
  public ResponseEntity<ApiResponse<Like>> comment(@PathVariable(name = "postId") String post, HttpServletRequest request){
    Like newLike = likeService.likeOrUnlike(post, request);
    ApiResponse<Like> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newLike);
    response.setMessage("A post has been liked by a user");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/post/{postId}/likes/count")
  public ResponseEntity<ApiResponse<Integer>> countLikes(@PathVariable(name = "postId") String post, HttpServletRequest request){
    int numOfLikes = likeService.countPostLikes(post, request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfLikes);
    response.setMessage("All likes have been counted for post of id : "+post);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
