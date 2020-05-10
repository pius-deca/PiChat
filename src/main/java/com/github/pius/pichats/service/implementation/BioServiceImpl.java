package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.BioRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.BioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class BioServiceImpl implements BioService {
  private BioRepository bioRepository;
  private JwtProvider jwtProvider;

  @Autowired
  public BioServiceImpl(BioRepository bioRepository, JwtProvider jwtProvider) {
    this.bioRepository = bioRepository;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Bio addOrUpdate(Bio bio, HttpServletRequest request) {
    try{
      User user = jwtProvider.resolveUser(request);
      Bio newBio = new Bio();
      return this.saveBio(bio, user, newBio);
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  private Bio saveBio(Bio bio, User user, Bio newBio) {
    Optional<Bio> userBio = bioRepository.findByUser(user);
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
    try{
      User user = jwtProvider.resolveUser(request);
      return bioRepository.findByUser(user).orElseThrow(() -> new CustomException(user.getUsername()+" has not set up his/her bio", HttpStatus.NOT_FOUND));
    }catch (Exception ex){
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
