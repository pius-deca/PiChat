package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentTest {
  private final static Comment comment = new Comment();
  private final static Post post = new Post();
  private final static User user1 = new User();
  private final static User user2 = new User();
  private static Validator validator;

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

    user1.setFirstName("firstname");
    user1.setLastName("lastname");
    user1.setEmail("user@mail.com");
    user1.setUsername("username");
    user1.setPassword("password");

    user2.setFirstName("firstname2");
    user2.setLastName("lastname2");
    user2.setEmail("user2@mail.com");
    user2.setUsername("username2");
    user2.setPassword("password2");

    post.setPost("new post.jpg");
    post.setCaption("caption");
    post.setUser(user1);
  }

  @Test
  @Order(1)
  @DisplayName("make a comment")
  void CommentOnAPost() {
    comment.setId(3L);
    comment.setComment("new comment");
    comment.setPost(post);
    comment.setUser(user2);

    assertEquals(comment.getId(), 3L);
    assertEquals(comment.getPost().getPost(), "new post.jpg");
    assertEquals(comment.getPost().getCaption(), "caption");
    assertEquals(comment.getPost().getUser().getFirstName(), "firstname");
    assertEquals(comment.getPost().getUser().getLastName(), "lastname");
    assertEquals(comment.getPost().getUser().getEmail(), "user@mail.com");
    assertEquals(comment.getPost().getUser().getUsername(), "username");
    assertEquals(comment.getUser().getFirstName(), "firstname2");
    assertEquals(comment.getUser().getLastName(), "lastname2");
    assertEquals(comment.getUser().getEmail(), "user2@mail.com");
    assertEquals(comment.getUser().getUsername(), "username2");
  }

  @Test
  @Order(2)
  @DisplayName("test if comment is null")
  void ifCommentIsNUll() {
    comment.setId(3L);
    comment.setComment(null);

    Set<ConstraintViolation<Comment>> violations = validator.validate(comment);
    assertEquals(violations.size(), 1);
  }
}
