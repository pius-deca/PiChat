package com.github.pius.pichats.service;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.CommentRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {
  private CommentRepository commentRepository;
  private UserRepository userRepository;
  private JwtProvider jwtProvider;
  private PostService postService;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, JwtProvider jwtProvider, PostService postService) {
    this.commentRepository = commentRepository;
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.postService = postService;
  }

  // this method enables a user find a post made by any user and make comments on it
  @Override
  public Comment makeComment(String post, Comment comment, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);

      // find any post to comment on
      Post postFound = postService.getPost(post);
      Comment newComment = new Comment();
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          newComment.setComment(comment.getComment());
          newComment.setPost(postFound);
          newComment.setUser(userByUsername.get());
          return commentRepository.save(newComment);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        newComment.setComment(comment.getComment());
        newComment.setPost(postFound);
        newComment.setUser(userByEmail.get());
        return commentRepository.save(newComment);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method finds a post user has made and get all comments on the post
  @Override
  public List<Comment> getAllCommentsForAPost(String post, HttpServletRequest request) {
    return commentRepository.findByPost(postService.findPost(post, request));
  }

  @Override
  public int countPostComments(String post, HttpServletRequest request) {
    Post postFound = postService.findPost(post, request);
    return commentRepository.countCommentsByPost(postFound);
  }
}
