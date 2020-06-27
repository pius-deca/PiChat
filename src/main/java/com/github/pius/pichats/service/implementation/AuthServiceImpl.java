package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.dto.SignupRequestDTO;
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
import com.github.pius.pichats.service.Utils.CodeGenerator;
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
import javax.validation.Valid;
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
  private CodeGenerator codeGenerator;

  @Autowired
  public AuthServiceImpl(JwtProvider jwtProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, EmailVerificationRepository emailVerificationRepository, EmailSenderService emailSenderService, BioRepository bioRepository, CodeGenerator codeGenerator) {
    this.jwtProvider = jwtProvider;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.emailVerificationRepository = emailVerificationRepository;
    this.emailSenderService = emailSenderService;
    this.bioRepository = bioRepository;
    this.codeGenerator = codeGenerator;
  }

//  @Transactional
  @Override
  public User register(SignupRequestDTO user) throws Exception{
    try{
      User newUser = new User();
      if (userRepository.existsByUsername(user.getUsername().toLowerCase())){
        throw new CustomException(user.getUsername() + " already exists", HttpStatus.BAD_REQUEST);
      }
      if (userRepository.existsByEmail(user.getEmail().toLowerCase())){
        throw new CustomException(user.getEmail() + " already exists", HttpStatus.BAD_REQUEST);
      }

      String code = codeGenerator.activationToken();
       // send email to the user asynchronously
      newUser.setEmail(user.getEmail().toLowerCase());
      newUser.setFirstName(user.getFirstName());
      newUser.setLastName(user.getLastName());
      newUser.setPassword(passwordEncoder.encode(user.getPassword()));
      newUser.setUsername(user.getUsername().toLowerCase());
      EmailVerification em = new EmailVerification();
      em.setValidity(LocalDateTime.now().plusHours(15));
      em.setCode(code);
      em.setUser(newUser);
      emailVerificationRepository.save(em);
//        this.emailSenderService.sendMail(newUser.getEmail(), "Activate pichat account", "Use the code below to activate your pichat account"+ "\n"+code);
//      Bio bio = new Bio();
//      bio.setUser(newUser);
//      bioRepository.save(bio);
      return userRepository.save(newUser);
    } catch (MailSendException ex){
      throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  @Override
  public String activate(String code, HttpServletRequest request) {
    User user = jwtProvider.resolveUser(request);
    Optional<EmailVerification> em = emailVerificationRepository.findByCodeAndUser(code, user);
    if (!em.isPresent()){
      throw new CustomException("Activation code is wrong", HttpStatus.BAD_REQUEST);
    }
    EmailVerification e = em.get();
    if (e.getValidity().isBefore(LocalDateTime.now())) {
      throw new CustomException("Activation code has expired", HttpStatus.BAD_REQUEST);
    }
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

//  public void isAccountActive(HttpServletRequest request){
//    User user = jwtProvider.resolveUser(request);
//    if (!user.isActive()) {
//      throw new CustomException("Your account is yet to be activated", HttpStatus.UNAUTHORIZED);
//    }
//  }
}
