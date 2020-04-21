package com.github.pius.pichats.security;

import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetails implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  private UserDetails GetDetails(String identifier, String password, String role) {
    return org.springframework.security.core.userdetails.User.withUsername(identifier).password(password)
      .authorities(new GrantedAuthority(){
        @Override
        public String getAuthority() {
          return role;
        }
      })
      .accountExpired(false).accountLocked(false).credentialsExpired(false)
      .disabled(false).build();
  }

  @Override
  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
    Optional<User> userByEmail = userRepository.findByEmail(identifier);
    if (!userByEmail.isPresent()){
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (userByUsername.isPresent()){
        User uUsername = userByUsername.get();
        return GetDetails(identifier, uUsername.getPassword(), "USER");
      }
    }else {
      User uEmail = userByEmail.get();
      return GetDetails(identifier, uEmail.getPassword(), "USER");
    }
    throw new UsernameNotFoundException("User with '"+identifier+"' not found");
  }
}
