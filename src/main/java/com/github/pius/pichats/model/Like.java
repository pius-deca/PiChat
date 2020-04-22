package com.github.pius.pichats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "likes")
public class Like extends GeneralBaseEntity{

  @Column(columnDefinition = "boolean default false", nullable = false)
  private boolean likes;

  private String user_identifier;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;
}
