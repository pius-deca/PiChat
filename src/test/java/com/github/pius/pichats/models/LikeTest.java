package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LikeTest {
  private final static Like like = new Like();
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
  @DisplayName("like a post")
  void LikeAPost() {
    like.setId(3L);
    like.setLikes(true);
    like.setPost(post);
    like.setUser(user2);

    assertEquals(like.getId(), 3L);
    assertTrue(like.isLikes());
    assertEquals(like.getPost().getPost(), "new post.jpg");
    assertEquals(like.getPost().getCaption(), "caption");
    assertEquals(like.getPost().getUser().getFirstName(), "firstname");
    assertEquals(like.getPost().getUser().getLastName(), "lastname");
    assertEquals(like.getPost().getUser().getEmail(), "user@mail.com");
    assertEquals(like.getPost().getUser().getUsername(), "username");
    assertEquals(like.getUser().getFirstName(), "firstname2");
    assertEquals(like.getUser().getLastName(), "lastname2");
    assertEquals(like.getUser().getEmail(), "user2@mail.com");
    assertEquals(like.getUser().getUsername(), "username2");
  }

}
