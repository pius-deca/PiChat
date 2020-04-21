package com.github.pius.pichats.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtFilterConfigurer  extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

  private JwtProvider jwtProvider;

  public JwtFilterConfigurer(JwtProvider jwtProvider){
    this.jwtProvider = jwtProvider;
  }

  @Override
  public void configure(HttpSecurity builder) throws Exception {
    JwtFilter tokenFilter = new JwtFilter(jwtProvider);
    builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
