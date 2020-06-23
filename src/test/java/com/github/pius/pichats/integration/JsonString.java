package com.github.pius.pichats.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonString {

  public static String jsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
