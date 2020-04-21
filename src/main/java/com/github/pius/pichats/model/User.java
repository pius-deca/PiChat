package com.github.pius.pichats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends GeneralBaseEntity{

  @NotBlank(message = "Please provide first name")
  @Column(nullable = false)
  private String first_name;

  @NotBlank(message = "Please provide last name")
  @Column(nullable = false)
  private String last_name;

  @Email(message = "Email should be valid")
  @Column(nullable = false)
  private String email;

  @NotBlank(message = "Please provide a username")
  @Column(nullable = false)
  private String username;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
  private List<Post> posts;
}
