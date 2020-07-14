package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class FollowController {
  private FollowService followService;

  @Autowired
  public FollowController(FollowService followService) {
    this.followService = followService;
  }

  @PostMapping("/{username}/follow")
  public ResponseEntity<ApiResponse<Follow>> follow(@PathVariable(name = "username") String username, HttpServletRequest request){
    Follow follow = followService.follow(username, request);
    ApiResponse<Follow> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(follow);
    response.setMessage("User has sent a request to follow '"+username+"'");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}/follow/accept")
  public ResponseEntity<ApiResponse<Follow>> acceptRequest(@PathVariable(name = "username") String username, HttpServletRequest request){
    Follow follow = followService.acceptRequest(username, request);
    ApiResponse<Follow> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(follow);
    response.setMessage("User has accepted '"+username+"' request");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}/follow/decline")
  public ResponseEntity<ApiResponse<String>> declineRequest(@PathVariable(name = "username") String username, HttpServletRequest request){
    String message = followService.declineRequest(username, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping("/{username}/unfollow")
  public ResponseEntity<ApiResponse<String>> unFollow(@PathVariable(name = "username") String username, HttpServletRequest request){
    String message = followService.unFollow(username, request);
    ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK);
    response.setMessage(message);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

//  @GetMapping("/followers")
//  public ResponseEntity<ApiResponse<Integer>> followers(HttpServletRequest request){
//    int numOfFollowers = followService.countFollowers(request);
//    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
//    response.setData(numOfFollowers);
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }
//
//  @GetMapping("/following")
//  public ResponseEntity<ApiResponse<Integer>> following(HttpServletRequest request){
//    int numOfFollowing = followService.countFollowing(request);
//    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
//    response.setData(numOfFollowing);
//    return new ResponseEntity<>(response, HttpStatus.OK);
//  }

  @GetMapping("/{username}/followers")
  public ResponseEntity<ApiResponse<Integer>> followers(@PathVariable(name = "username") String username, HttpServletRequest request){
    int numOfFollowers = followService.countFollowersOfSearchedUser(username, request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowers);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @GetMapping("/{username}/following")
  public ResponseEntity<ApiResponse<Integer>> following(@PathVariable(name = "username") String username, HttpServletRequest request){
    int numOfFollowing = followService.countFollowingSearchedUser(username, request);
    ApiResponse<Integer> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(numOfFollowing);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
