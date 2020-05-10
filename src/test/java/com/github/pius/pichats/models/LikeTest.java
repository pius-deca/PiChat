package com.github.pius.pichats.models;

import com.github.pius.pichats.model.Like;
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
  private static Validator validator;

  @BeforeAll
  static void initAll() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  @Order(1)
  @DisplayName("like a post")
  void LikeAPost() {
    like.setId(3L);
    like.setLikes(true);

    assertEquals(like.getId(), 3L);
    assertTrue(like.isLikes());
  }

}
