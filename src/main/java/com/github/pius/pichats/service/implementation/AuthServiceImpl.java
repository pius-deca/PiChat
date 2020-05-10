package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

  private JwtProvider jwtProvider;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;

  @Autowired
  public AuthServiceImpl(JwtProvider jwtProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public User register(User user) {
    try{
      if (!(userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail()))){
        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getUsername());
        return userRepository.save(newUser);
      }
      throw new CustomException("User already exists", HttpStatus.BAD_REQUEST);
    } catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public LoginResponseDTO login(LoginRequestDTO user) {
    String identifier = user.getIdentifier();
    String password = user.getPassword();
    try{
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        identifier, password
      ));
      Optional<User> authUserByEmail = userRepository.findByEmail(identifier);
      Optional<User> authUserByUsername = userRepository.findByUsername(identifier);
      String token = jwtProvider.createToken(identifier);
      if (!authUserByEmail.isPresent()){
        if (authUserByUsername.isPresent()){
          LoginResponseDTO loginResponse = new LoginResponseDTO(authUserByUsername.get().getFirstName(), authUserByUsername.get().getLastName(), authUserByUsername.get().getEmail(), authUserByUsername.get().getUsername(), null);
          loginResponse.setToken(token);
          return loginResponse;
        }
        throw new CustomException("Invalid email or username/password supplied...", HttpStatus.UNPROCESSABLE_ENTITY);
      }
      LoginResponseDTO loginResponse = new LoginResponseDTO(authUserByEmail.get().getFirstName(), authUserByEmail.get().getLastName(), authUserByEmail.get().getEmail(), authUserByEmail.get().getUsername(), null);
      loginResponse.setToken(token);
      return loginResponse;
    } catch (Exception ex){
      throw new CustomException("Invalid email or username/password supplied...", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }
}
