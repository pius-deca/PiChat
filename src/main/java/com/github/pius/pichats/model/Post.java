package com.github.pius.pichats.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "posts")
public class Post extends GeneralBaseEntity{

  private String caption;

  @NotBlank(message = "Please provide a post")
  private String post;

  private String url;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, mappedBy = "post", orphanRemoval = true)
  @JsonIgnore
  private List<Comment> comments;

  @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
  @JsonIgnore
  private List<Like> likes;
}
