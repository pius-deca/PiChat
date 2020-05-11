package com.github.pius.pichats.models;

import com.github.pius.pichats.model.GeneralBaseEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeneralBaseEntityTest {
  private final static GeneralBaseEntity generalBaseEntity = new GeneralBaseEntity();
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
    generalBaseEntity.setId(3L);

    assertEquals(generalBaseEntity.getId(), 3L);
  }
}