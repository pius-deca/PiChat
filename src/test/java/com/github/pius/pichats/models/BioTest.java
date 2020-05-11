package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BioTest {
  private final static Bio bio = new Bio();
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
  @DisplayName("create a user bio")
  void createAUserBio() {
    bio.setId(3L);
    bio.setPhone("0810987654");
    bio.setUser(user);

    assertEquals(bio.getId(), 3L);
    assertEquals(bio.getPhone(), "0810987654");
    assertEquals(bio.getUser().getUsername(), "username");
    assertEquals(bio.getUser().getEmail(), "user@mail.com");
    assertEquals(bio.getUser().getFirstName(), "firstname");
    assertEquals(bio.getUser().getLastName(), "lastname");
  }

}
