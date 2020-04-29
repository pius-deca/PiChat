package com.github.pius.pichats.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
  private JwtProvider jwtProvider;

  public JwtFilter(JwtProvider jwtProvider){
    this.jwtProvider = jwtProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
    String token = jwtProvider.resolveToken(httpServletRequest);
    try {
      if (token != null && jwtProvider.validateToken(token)) {
        Authentication auth = jwtProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (Exception e) {
      // this is very important, since it guarantees the user is not authenticated at
      // all
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not authenticated");// , e.getMessage());
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
