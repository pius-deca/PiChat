package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.dto.ChangePasswordDTO;
import com.github.pius.pichats.dto.UpdateRequestDTO;
import com.github.pius.pichats.dto.UpdateResponseDTO;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.EmailVerification;
import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.EmailVerificationRepository;
import com.github.pius.pichats.repository.FollowRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.UserService;
import com.github.pius.pichats.service.Utils.CodeGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final PasswordEncoder passwordEncoder;
  private final FollowRepository followRepository;
  private final CodeGenerator codeGenerator;
  private final EmailVerificationRepository emailVerificationRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder,
      FollowRepository followRepository, CodeGenerator codeGenerator,
      EmailVerificationRepository emailVerificationRepository) {
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
    this.passwordEncoder = passwordEncoder;
    this.followRepository = followRepository;
    this.codeGenerator = codeGenerator;
    this.emailVerificationRepository = emailVerificationRepository;
  }

  // check if user is active
  @Override
  public boolean isActive(HttpServletRequest request) {
    User user = jwtProvider.resolveUser(request);
    return user.isActive();
  }

  @Override
  public User searchByUsername(String username, HttpServletRequest request) {
    try {
      jwtProvider.resolveUser(request);
      Optional<User> searchedUser = userRepository.findByUsername(username.toLowerCase());
      if (searchedUser.isPresent()) {
        return searchedUser.get();
      }
      throw new CustomException("The user : '" + username + "' you are searching for does not exists",
          HttpStatus.NOT_FOUND);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public List<User> searchUsernameByString(String username, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      if (!username.isEmpty()) {
        List<User> users = userRepository.searchByUsername(username);
        return users.stream().filter(u -> u != user).collect(Collectors.toList());
      }
      return new ArrayList<>();
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public String changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      return newPassword(passwordDTO, user);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  private String newPassword(ChangePasswordDTO passwordDTO, User user) {
    if (passwordEncoder.matches(passwordDTO.getCurrentPassword(), user.getPassword())) {
      if (passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
        return user.getUsername() + " has changed password";
      }
      throw new CustomException("New Password not yet confirmed, passwords must match", HttpStatus.BAD_REQUEST);
    }
    throw new CustomException("Current password is not correct, please enter the correct password",
        HttpStatus.BAD_REQUEST);
  }

  @Override
  public UpdateResponseDTO updateUser(UpdateRequestDTO updateRequestDTO, HttpServletRequest request) {
    User user = jwtProvider.resolveUser(request);
    try {
      if (!(user.getEmail().equalsIgnoreCase(updateRequestDTO.getEmail()))) {
        if (userRepository.existsByEmail(updateRequestDTO.getEmail().toLowerCase())) {
          throw new CustomException(updateRequestDTO.getEmail() + " already exists", HttpStatus.BAD_REQUEST);
        }
        // generate activate code if email is updated
        String code = codeGenerator.activationToken();
        // create token if email is updated
        String token = jwtProvider.createToken(user.getEmail());
        updateRequestDTO.setToken(token);
        // set user account to false if email is updated
        user.setActive(false);
        Optional<EmailVerification> em = emailVerificationRepository.findByUser(user);
        if (em.isPresent()) {
          em.get().setCode(code);
          em.get().setValidity(LocalDateTime.now().plusHours(15));
          emailVerificationRepository.save(em.get());
        }
        throw new CustomException("Email is not verified", HttpStatus.BAD_REQUEST);
      } else if (!(user.getUsername().equalsIgnoreCase(updateRequestDTO.getUsername()))) {
        if (userRepository.existsByUsername(updateRequestDTO.getUsername().toLowerCase())) {
          throw new CustomException(updateRequestDTO.getUsername() + " already exists", HttpStatus.BAD_REQUEST);
        }
        // create token if username is updated
        String token = jwtProvider.createToken(user.getUsername());
        updateRequestDTO.setToken(token);
      }
      // update users account details
      user.setFirstName(updateRequestDTO.getFirstName());
      user.setLastName(updateRequestDTO.getLastName());
      user.setEmail(updateRequestDTO.getEmail().toLowerCase());
      user.setUsername(updateRequestDTO.getUsername().toLowerCase());
      // save the updated user
      User updatedUser = userRepository.save(user);
      // find all details in follows table relating to user's username
      List<Follow> followList = followRepository.findAllByFollowing(updatedUser.getUsername());
      // for each detail found update it with the updated user's username
      for (Follow following : followList) {
        following.setFollowing(updatedUser.getUsername());
      }
      // save the follows table
      followRepository.saveAll(followList);
      return new UpdateResponseDTO(updatedUser.getId(), updatedUser.getFirstName(), updatedUser.getLastName(),
          updatedUser.getEmail(), updatedUser.getUsername(), updatedUser.isActive(), updatedUser.getCreatedAt(),
          updatedUser.getUpdatedAt(), updateRequestDTO.getToken());
    } catch (Exception e) {
      throw new CustomException("Error trying to update '" + user.getUsername() + "'", HttpStatus.BAD_REQUEST);
    }
  }

}
