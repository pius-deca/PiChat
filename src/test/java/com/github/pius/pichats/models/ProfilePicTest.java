package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.ProfilePic;
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

public class ProfilePicTest {
  private final static ProfilePic profile = new ProfilePic();
  private final static User user = new User();
  private static Validator validator;

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();

    user.setFirstName("firstname");
    user.setLastName("lastname");
    user.setEmail("user@mail.com");
    user.setUsername("username");
    user.setPassword("password");
  }

  @Test
  @Order(1)
  @DisplayName("create a profile")
  void createAProfile() {
    profile.setId(3L);
    profile.setProfilePic("new profile.jpg");
    profile.setUser(user);

    assertEquals(profile.getId(), 3L);
    assertEquals(profile.getProfilePic(), "new profile.jpg");
    assertEquals(profile.getUser().getUsername(), "username");
    assertEquals(profile.getUser().getEmail(), "user@mail.com");
    assertEquals(profile.getUser().getFirstName(), "firstname");
    assertEquals(profile.getUser().getLastName(), "lastname");
  }

  @Test
  @Order(2)
  @DisplayName("test if profile picture is null")
  void ifProfilePictureIsNUll() {
    profile.setId(3L);
    profile.setProfilePic(null);

    Set<ConstraintViolation<ProfilePic>> violations = validator.validate(profile);
    assertEquals(violations.size(), 1);
  }

}
