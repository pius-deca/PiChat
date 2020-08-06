package com.github.pius.pichats.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateResponseDTO extends AuthResponseDTO {
  private String token;

  public UpdateResponseDTO(String firstName, String lastName, String email, String username, String password, boolean active, String token, Long id, Timestamp created_at, Timestamp updated_at) {
    super(firstName, lastName, email, username, password, active, id, created_at, updated_at);
    this.token = token;
  }
}
