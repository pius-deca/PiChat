package com.github.pius.pichats.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.github.pius.pichats.model.User;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class UserTest {
  private final static User user = new User();
  private static Validator validator;

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @Order(1)
  @DisplayName("create a user")
  void createAUser() {
    user.setId(3L);
    user.setFirstName("firstname");
    user.setLastName("lastname");
    user.setEmail("user@mail.com");
    user.setUsername("username");
    user.setPassword("password");

    assertEquals(user.getId(), 3L);
    assertEquals(user.getUsername(), "username");
    assertEquals(user.getEmail(), "user@mail.com");
    assertEquals(user.getFirstName(), "firstname");
    assertEquals(user.getLastName(), "lastname");
  }

  @Test
  @Order(2)
  @DisplayName("test if fields are null")
  void ifPostIsNUll() {
    user.setId(3L);
    user.setFirstName("firstname");
    user.setLastName("lastname");
    user.setEmail(null);
    user.setUsername(null);
    user.setPassword(null);

    Set<ConstraintViolation<User>> violations = validator.validate(user);
    assertEquals(violations.size(), 3);
  }

}
