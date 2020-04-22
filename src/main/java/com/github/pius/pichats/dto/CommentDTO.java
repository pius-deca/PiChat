package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
  @NotBlank(message = "Please provide a comment")
  private String comment;
}
