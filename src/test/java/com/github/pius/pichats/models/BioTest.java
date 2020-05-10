package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Bio;
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
  private static Validator validator;

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @Order(1)
  @DisplayName("create a user bio")
  void createAUserBio() {
    bio.setId(3L);
    bio.setPhone("0810987654");

    assertEquals(bio.getId(), 3L);
    assertEquals(bio.getPhone(), "0810987654");
  }

}
