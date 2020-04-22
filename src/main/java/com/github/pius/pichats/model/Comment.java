package com.github.pius.pichats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
public class Comment extends GeneralBaseEntity{

  @NotBlank(message = "Please provide a comment")
  @Column(nullable = false)
  private String comment;

  private String user_identifier;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

}
