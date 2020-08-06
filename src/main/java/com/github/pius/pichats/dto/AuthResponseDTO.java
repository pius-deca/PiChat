package com.github.pius.pichats.dto;

import com.github.pius.pichats.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthResponseDTO extends User {
  private String token;

  public AuthResponseDTO(String firstName, String lastName, String email, String username, String password, boolean active, Long id, Timestamp created_at, Timestamp updated_at) {
    super(firstName, lastName, email, username, password, active);
    this.id = id;
    this.createdAt = created_at;
    this.updatedAt = updated_at;
  }
}
