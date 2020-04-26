package com.github.pius.pichats.service;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.BioRepository;
import com.github.pius.pichats.repository.UserRepository;
import com.github.pius.pichats.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class BioServiceImpl implements BioService {
  private BioRepository bioRepository;
  private UserRepository userRepository;
  private JwtProvider jwtProvider;

  @Autowired
  public BioServiceImpl(BioRepository bioRepository, UserRepository userRepository, JwtProvider jwtProvider) {
    this.bioRepository = bioRepository;
    this.userRepository = userRepository;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Bio addOrUpdate(Bio bio, HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      Bio newBio = new Bio();
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
          return saveBio(bio, userByUsername, newBio, userByUsername.get());
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        return saveBio(bio, userByEmail, newBio, userByEmail.get());
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  private Bio saveBio(Bio bio, Optional<User> identifier, Bio newBio, User user) {
    Optional<Bio> userBio = bioRepository.findByUser(identifier.get());
    if (userBio.isPresent()){
      userBio.get().setDescription(bio.getDescription());
      userBio.get().setAddress(bio.getAddress());
      userBio.get().setCountry(bio.getCountry());
      userBio.get().setDob(bio.getDob());
      userBio.get().setPhone(bio.getPhone());
      return bioRepository.save(userBio.get());
    }
    newBio.setDescription(bio.getDescription());
    newBio.setAddress(bio.getAddress());
    newBio.setCountry(bio.getCountry());
    newBio.setDob(bio.getDob());
    newBio.setPhone(bio.getPhone());
    newBio.setUser(user);
    return bioRepository.save(newBio);
  }

  @Override
  public Bio find(HttpServletRequest request) {
    String token = jwtProvider.resolveToken(request);
    String identifier = jwtProvider.getIdentifier(token);
    try{
      Optional<User> userByEmail = userRepository.findByEmail(identifier);
      Optional<User> userByUsername = userRepository.findByUsername(identifier);
      if (!userByEmail.isPresent()){
        if (userByUsername.isPresent()){
;         return bioRepository.findByUser(userByUsername.get()).orElseThrow(() -> new CustomException(identifier+" has not set up his/her bio", HttpStatus.NOT_FOUND));
        }
        throw new CustomException("User does not exists", HttpStatus.NOT_FOUND);
      }else{
        return bioRepository.findByUser(userByEmail.get()).orElseThrow(() -> new CustomException(identifier+" has not set up his/her bio", HttpStatus.NOT_FOUND));
      }
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
