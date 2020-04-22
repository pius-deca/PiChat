package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.repository.PostRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class PostServiceImpl implements PostService {
  private JwtProvider jwtProvider;
  private UserRepository userRepository;
  private PostRepository postRepository;

  @Autowired
  public PostServiceImpl(JwtProvider jwtProvider, UserRepository userRepository, PostRepository postRepository) {
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  @Override
  public Post post(Post post, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);

    if (userRepository.existsByEmail(identifier) || userRepository.existsByUsername(identifier)){
      Post newPost = new Post();
      newPost.setCaption(post.getCaption());
      newPost.setPost(post.getPost());
    }
  }
}
