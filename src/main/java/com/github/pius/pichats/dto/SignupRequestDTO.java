package com.github.pius.pichats.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SignupRequestDTO {
  @NotBlank(message = "Please provide first name")
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "Please provide last name")
  @Column(name = "last_name")
  private String lastName;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Please provide an email")
  private String email;

  @NotBlank(message = "Please provide a username")
  private String username;

  @NotBlank(message = "Please provide a password")
  private String password;

  public SignupRequestDTO(String firstName, String lastName, String email, String username, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
  }
}
