package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchUsernameDto {

  @NotBlank(message = "Please provide a username to search for")
  private String username;
}
