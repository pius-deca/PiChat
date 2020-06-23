package com.github.pius.pichats.integration;

import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDetails {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtProvider jwtProvider;

  @Autowired
  private UserRepository userRepository;

  public User createUser(String identifier) {
    User user = new User();
    user.setPassword(passwordEncoder.encode("password"));
    user.setFirstName("firstname");
    user.setLastName("lastname");
    user.setUsername("username");
    user.setEmail(identifier);
    return userRepository.save(user);
  }

  public String getLoggedInToken(String identifier) {
    this.createUser(identifier);
    return jwtProvider.createToken(identifier);
  }
}
