package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.EmailVerification;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.BioRepository;
import com.github.pius.pichats.repository.EmailVerificationRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.AuthService;
import com.github.pius.pichats.service.EmailSenderService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

  private JwtProvider jwtProvider;
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;
  private AuthenticationManager authenticationManager;
  private EmailVerificationRepository emailVerificationRepository;
  private BioRepository bioRepository;
  private EmailSenderService emailSenderService;

  @Autowired
  public AuthServiceImpl(JwtProvider jwtProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailVerificationRepository emailVerificationRepository, EmailSenderService emailSenderService, BioRepository bioRepository) {
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.emailVerificationRepository = emailVerificationRepository;
    this.emailSenderService = emailSenderService;
    this.bioRepository = bioRepository;
  }

  private String activationToken(){
    return RandomStringUtils.randomAlphanumeric(6);
  }

  @Transactional
  @Override
  public User register(User user) throws MailSendException{
    try{//
      if (!(userRepository.existsByUsername(user.getUsername()) || userRepository.existsByEmail(user.getEmail()))){
        String token = activationToken();
         // send email to the user asynchronously
        User newUser = new User();
        newUser.setEmail(user.getEmail().toLowerCase());
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setUsername(user.getUsername().toLowerCase());
        EmailVerification em = new EmailVerification();
        em.setEmail(newUser.getEmail());
        em.setValidity(LocalDateTime.now().plusHours(24));
        em.setCode(token);
        em.setUser(newUser);
        emailVerificationRepository.save(em);
        this.emailSenderService.sendMail(newUser.getEmail(), "Activate pichat account", "Use the code below to activate your pichat account"+ "\n"+token);
        Bio bio = new Bio();
        bio.setUser(newUser);
        bioRepository.save(bio);
        return userRepository.save(newUser);
      }
      Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
      Optional<User> existingEmail = userRepository.findByEmail(user.getEmail());
      if (existingUsername.isPresent()){
        if (existingUsername.get().getUsername().equals(user.getUsername().toLowerCase())){
          throw new CustomException(user.getUsername() + " already exists", HttpStatus.BAD_REQUEST);
        }
      }
      if (existingEmail.isPresent()){
        if (existingEmail.get().getEmail().equals(user.getEmail().toLowerCase())){
          throw new CustomException(user.getEmail() + " already exists", HttpStatus.BAD_REQUEST);
        }
      }
      throw new CustomException("user already exists", HttpStatus.BAD_REQUEST);
    } catch (MailSendException ex){
      throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public String activate(String code) {
    Optional<EmailVerification> em = emailVerificationRepository.findByCode(code);
    if (!em.isPresent()){
      throw new CustomException("Activation code is wrong", HttpStatus.BAD_REQUEST);
    }
    EmailVerification e = em.get();
    if (e.getValidity().isBefore(LocalDateTime.now())) {
      throw new CustomException("Activation code has expired", HttpStatus.BAD_REQUEST);
    }
    User user = e.getUser();
    user.setActive(true);
    userRepository.save(user);
    return "Account has been activated";
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
      if (!authUserByEmail.isPresent()){
        if (authUserByUsername.isPresent()){
          LoginResponseDTO loginResponse = new LoginResponseDTO(authUserByUsername.get().getFirstName(), authUserByUsername.get().getLastName(), authUserByUsername.get().getEmail(), authUserByUsername.get().getUsername(), null);
          String token = jwtProvider.createToken(loginResponse.getUsername());
          loginResponse.setToken(token);
          return loginResponse;
        }
        throw new CustomException("Invalid email or username/password supplied...", HttpStatus.UNPROCESSABLE_ENTITY);
      }
      LoginResponseDTO loginResponse = new LoginResponseDTO(authUserByEmail.get().getFirstName(), authUserByEmail.get().getLastName(), authUserByEmail.get().getEmail(), authUserByEmail.get().getUsername(), null);
      String token = jwtProvider.createToken(loginResponse.getUsername());
      loginResponse.setToken(token);
      return loginResponse;
    } catch (Exception ex){
      throw new CustomException("Invalid email or username/password supplied...", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void isAccountActive(HttpServletRequest request){
    User user = jwtProvider.resolveUser(request);
    if (!user.isActive()) {
      throw new CustomException("Your account is yet to be activated", HttpStatus.UNAUTHORIZED);
    }
  }
}
