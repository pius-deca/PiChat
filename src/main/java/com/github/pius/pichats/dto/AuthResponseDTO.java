package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {

  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private boolean active;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  private String token;
}
