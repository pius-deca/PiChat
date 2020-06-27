package com.github.pius.pichats.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateRequestDTO{
  @NotBlank(message = "Please provide first name")
  private String firstName;

  @NotBlank(message = "Please provide last name")
  private String lastName;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Please provide an email")
  private String email;

  @NotBlank(message = "Please provide a username")
  private String username;

  private String token;
}
