package com.github.pius.pichats.dto;

import com.github.pius.pichats.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilePicDTO {

  @NotBlank(message = "Please provide a profile picture")
  private String profilePic;

  private User user;
}
