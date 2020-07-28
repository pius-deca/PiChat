package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.CommentResponseDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.CommentRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.CommentService;
import com.github.pius.pichats.service.PostService;
import com.github.pius.pichats.service.Utils.EntityPageIntoDtoPage;
import com.github.pius.pichats.service.Utils.PageResultConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;
  private final JwtProvider jwtProvider;
  private final PostService postService;
  private final AuthServiceImpl authServiceImpl;
  private final EntityPageIntoDtoPage entityPageIntoDtoPage;

  @Autowired
  public CommentServiceImpl(CommentRepository commentRepository, JwtProvider jwtProvider, PostService postService, AuthServiceImpl authServiceImpl, EntityPageIntoDtoPage entityPageIntoDtoPage) {
    this.commentRepository = commentRepository;
    this.jwtProvider = jwtProvider;
    this.postService = postService;
    this.authServiceImpl = authServiceImpl;
    this.entityPageIntoDtoPage = entityPageIntoDtoPage;
  }

  // this method enables a user find a post made by any user and make comments on it
  @Override
  public Comment makeComment(String post, Comment comment, HttpServletRequest request) {
    try{
//      authServiceImpl.isAccountActive(request);
      User user = jwtProvider.resolveUser(request);
      // find any post to comment on
      Post postFound = postService.getPost(post);
      Comment newComment = new Comment();
      newComment.setComment(comment.getComment());
      newComment.setPost(postFound);
      newComment.setUser(user);
      return commentRepository.save(newComment);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  // this method finds a post user has made and get all comments on the post
  @Override
  public PageResultConverter getAllCommentsForAPost(int page, int limit, String post, HttpServletRequest request) {
//    authServiceImpl.isAccountActive(request);
    if (page > 0) page--;

    Pageable pageable = PageRequest.of(page, limit);

    Page<Comment> entities = commentRepository.findByPostOrderByCreatedAtDesc(pageable, postService.findPost(post, request));

    Page<CommentResponseDTO> response = entityPageIntoDtoPage.mapEntityPageIntoDtoPage(entities, CommentResponseDTO.class);

    response.stream().forEach(res -> {
      res.setComment(res.getComment());
      res.setUser(res.getUser());
    });

    return new PageResultConverter(response);
  }

  @Override
  public int countPostComments(String post, HttpServletRequest request) {
//    authServiceImpl.isAccountActive(request);
    Post postFound = postService.findPost(post, request);
    return commentRepository.countCommentsByPost(postFound);
  }
}
