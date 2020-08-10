package com.github.pius.pichats.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateResponseDTO extends AuthResponseDTO {
  public UpdateResponseDTO(Long id, String firstName, String lastName, String email, String username, boolean active,
      Timestamp createdAt, Timestamp updatedAt, String token) {
    super(id, firstName, lastName, email, username, active, createdAt, updatedAt, token);
  }
}
