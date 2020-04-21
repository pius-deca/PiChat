package com.github.pius.pichats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends GeneralBaseEntity{

  @NotBlank(message = "Please provide a comment")
  @Column(nullable = false)
  private String comment;

  private String identifier;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

}
