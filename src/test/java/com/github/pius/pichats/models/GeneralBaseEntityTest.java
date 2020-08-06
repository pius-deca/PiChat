package com.github.pius.pichats.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import com.github.pius.pichats.model.GeneralBaseEntity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class GeneralBaseEntityTest {
  private final static GeneralBaseEntity generalBaseEntity = new GeneralBaseEntity();

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    factory.getValidator();
  }

  @Test
  @Order(1)
  @DisplayName("create a user bio")
  void createAUserBio() {
    generalBaseEntity.setId(3L);

    assertEquals(generalBaseEntity.getId(), 3L);
  }
}