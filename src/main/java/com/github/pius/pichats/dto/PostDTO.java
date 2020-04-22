package com.github.pius.pichats.dto;

import com.github.pius.pichats.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

  private String caption;

  @NotBlank(message = "Please provide a post")
  @Column(nullable = false)
  private String post;

  private User user;
}
