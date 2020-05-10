package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.Post;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PostTest {
  private final static Post post = new Post();
  private static Validator validator;

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @Order(1)
  @DisplayName("create a post")
  void createAPost() {
    post.setId(3L);
    post.setPost("new post.jpg");

    assertEquals(post.getId(), 3L);
    assertEquals(post.getPost(), "new post.jpg");
  }

  @Test
  @Order(1)
  @DisplayName("test if post is null")
  void ifPostIsNUll() {
    post.setId(3L);
    post.setPost(null);

    Set<ConstraintViolation<Post>> violations = validator.validate(post);
    assertEquals(violations.size(), 1);
  }
}
