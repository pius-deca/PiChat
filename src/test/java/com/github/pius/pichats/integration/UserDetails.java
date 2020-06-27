//package com.github.pius.pichats.integration;
//
//import com.github.pius.pichats.model.EmailVerification;
//import com.github.pius.pichats.model.User;
//import com.github.pius.pichats.repository.EmailVerificationRepository;
//import com.github.pius.pichats.repository.UserRepository;
//import com.github.pius.pichats.security.JwtProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class UserDetails {
//  @Autowired
//  private PasswordEncoder passwordEncoder;
//
//  @Autowired
//  private JwtProvider jwtProvider;
//
//  @Autowired
//  private UserRepository userRepository;
//
//  @Autowired
//  private EmailVerificationRepository emailVerificationRepository;
//
//  public User createUser(String email) {
//    User user = new User();
//    EmailVerification em = new EmailVerification();
//    user.setPassword(passwordEncoder.encode("password"));
//    user.setFirstName("firstname");
//    user.setLastName("lastname");
//    user.setUsername("username");
//    user.setEmail(email);
//    em.setCode("12345");
//    em.setValidity(LocalDateTime.now().plusHours(15));
//    em.setUser(user);
//    emailVerificationRepository.save(em);
//    return userRepository.save(user);
//  }
//
//  public String getLoggedInToken(String email) {
//    this.createUser(email);
//    return jwtProvider.createToken(email);
//  }
//}
