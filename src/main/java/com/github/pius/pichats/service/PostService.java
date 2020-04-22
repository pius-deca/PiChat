package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.model.Post;

import javax.servlet.http.HttpServletRequest;

public interface PostService {
  Post post(Post post, HttpServletRequest request);
}
