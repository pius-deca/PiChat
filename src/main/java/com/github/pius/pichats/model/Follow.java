package com.github.pius.pichats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "follows")
public class Follow extends GeneralBaseEntity {

  private String following;

  private String profilePic;

  private String followingBack = "false";

  @Column(columnDefinition = "boolean default false", nullable = false)
  private boolean accepted;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
