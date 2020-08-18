package com.github.pius.pichats.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class LikeTest {
  private final static Like like = new Like();
  private final static Post post = new Post();
  private final static User user1 = new User();
  private final static User user2 = new User();

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    factory.getValidator();

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
    like.setPost(post);
    like.setUser(user2);

    assertEquals(3L, like.getId());
    assertEquals("new post.jpg", like.getPost().getPost());
    assertEquals("caption", like.getPost().getCaption());
    assertEquals("firstname", like.getPost().getUser().getFirstName());
    assertEquals("lastname", like.getPost().getUser().getLastName());
    assertEquals("user@mail.com", like.getPost().getUser().getEmail());
    assertEquals("username", like.getPost().getUser().getUsername());
    assertEquals("firstname2", like.getUser().getFirstName());
    assertEquals("lastname2", like.getUser().getLastName());
    assertEquals("user2@mail.com", like.getUser().getEmail());
    assertEquals("username2", like.getUser().getUsername());
  }

}
