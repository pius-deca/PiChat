package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Follow;
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

import static org.junit.jupiter.api.Assertions.*;

public class FollowTest {
  private final static Follow follow = new Follow();
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
  }

  @Test
  @Order(1)
  @DisplayName("make a comment")
  void FollowAUser() {
    follow.setId(3L);
    follow.setUser(user1);
    follow.setFollowing(user2.getUsername());
    follow.setAccepted(false);

    assertEquals(follow.getId(), 3L);
    assertFalse(follow.isAccepted());
    assertEquals(follow.getUser().getFirstName(), "firstname");
    assertEquals(follow.getUser().getLastName(), "lastname");
    assertEquals(follow.getUser().getEmail(), "user@mail.com");
    assertEquals(follow.getUser().getUsername(), "username");
    assertEquals(follow.getFollowing(), "username2");
  }

}
