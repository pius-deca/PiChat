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

  @Override
  public Comment makeComment(Long postId, Comment comment, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      Post post = postService.findPostById(postId);
      Comment newComment = new Comment();
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          newComment.setComment(comment.getComment());
          newComment.setPost(post);
          newComment.setUser_identifier(identifier);
          return commentRepository.save(newComment);
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        newComment.setComment(comment.getComment());
        newComment.setPost(post);
        newComment.setUser_identifier(identifier);
        return commentRepository.save(newComment);
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
