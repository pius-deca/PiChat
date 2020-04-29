package com.github.pius.pichats.security;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtProvider {

  @Value("${api.security.jwt.token.secret-key:secret-key}")
  private String secretKey;

  @Value("${api.security.jwt.token.expire-length:86400000}")
  private long validityInMilliseconds = 86400000;

  @Autowired
  private UserDetailsService myUserDetails;

  @Autowired
  private UserRepository userRepository;

  protected String generateToken(String subject, Long validityPeriod) {
    Claims claims = Jwts.claims().setSubject(subject);
    Date now = new Date();
    Date validity = new Date(now.getTime() + validityPeriod);

    return Jwts.builder()
      .setClaims(claims)
      .setIssuedAt(now)
      .setExpiration(validity)
      .signWith(SignatureAlgorithm.HS256, secretKey)
      .compact();
  }

  public String createToken(String identifier) {
    return generateToken(identifier, validityInMilliseconds);
  }

  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  public Authentication getAuthentication(String token) {
    UserDetails userDetails = myUserDetails.loadUserByUsername(getIdentifier(token));
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  public String getIdentifier(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
  }

  private Claims getAllClaims(String token) {
    return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
  }

  public Boolean isTokenExpired(String token) {
    return getAllClaims(token).getExpiration().before(new Date());
  }

  public User resolveUser(HttpServletRequest request){
    String token = this.resolveToken(request);
    String identifier = this.getIdentifier(token);
    Optional<User> userByEmail = userRepository.findByEmail(identifier);
    if (!userByEmail.isPresent()){
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (userByUsername.isPresent()){
        return userByUsername.get();
      }
      throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
    }
    return userByEmail.get();
  }
}
