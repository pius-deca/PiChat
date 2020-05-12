package com.github.pius.pichats.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtProvider jwtProvider;


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/auth/**")
      .antMatchers("/swagger-config.yaml")
      .antMatchers("/api/v1/swagger-config.yaml")
      .antMatchers("/api-doc");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Disable CSRF (cross site request forgery)
    // enabled by default
    http.csrf().disable();

    // No session will be created or used by spring security
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // set authentication policies for each routes
    http.authorizeRequests().anyRequest().authenticated();

    http.apply(new JwtFilterConfigurer(jwtProvider));
  }

  @Bean
  public AuthenticationManager customAuthenticationManager() throws Exception {
    return authenticationManager();
  }
}
