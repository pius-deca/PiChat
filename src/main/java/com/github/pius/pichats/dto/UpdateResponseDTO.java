package com.github.pius.pichats.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateResponseDTO extends LoginResponseDTO {
  private String token;

  public UpdateResponseDTO(String firstName, String lastName, String email, String username, String password, String token) {
    super(firstName, lastName, email, username, password);
    this.token = token;
  }
}
