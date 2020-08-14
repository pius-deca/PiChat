package com.github.pius.pichats.service.implementation;

import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.Notification;
import com.github.pius.pichats.model.User;
import com.github.pius.pichats.repository.BioRepository;
import com.github.pius.pichats.repository.NotificationRepository;
import com.github.pius.pichats.security.JwtProvider;
import com.github.pius.pichats.service.BioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class BioServiceImpl implements BioService {
  private final BioRepository bioRepository;
  private final NotificationRepository notificationRepository;
  private final JwtProvider jwtProvider;

  @Autowired
  public BioServiceImpl(BioRepository bioRepository, NotificationRepository notificationRepository,
      JwtProvider jwtProvider) {
    this.bioRepository = bioRepository;
    this.notificationRepository = notificationRepository;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public Bio addOrUpdate(Bio bio, HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      Bio newBio = new Bio();
      return saveBio(bio, user, newBio);
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  private Bio saveBio(Bio bio, User user, Bio newBio) {
    Notification notify = new Notification();
    Optional<Bio> userBio = bioRepository.findByUser(user);
    if (userBio.isPresent()) {
      userBio.get().setDescription(bio.getDescription());
      userBio.get().setCountry(bio.getCountry());
      userBio.get().setDob(bio.getDob());
      userBio.get().setPhone(bio.getPhone());
      userBio.get().setGender(bio.getGender());

      // notify user that his/her bio was updated
      notify.setActor(user.getUsername());
      notify.setMessage("Bio was updated");
      notificationRepository.save(notify);
      return bioRepository.save(userBio.get());
    }
    newBio.setDescription(bio.getDescription());
    newBio.setCountry(bio.getCountry());
    newBio.setDob(bio.getDob());
    newBio.setPhone(bio.getPhone());
    newBio.setGender(bio.getGender());
    newBio.setUser(user);

    // notify user that his/her bio was created
    notify.setActor(user.getUsername());
    notify.setMessage("Bio was created");
    notificationRepository.save(notify);
    return bioRepository.save(newBio);
  }

  @Override
  public Bio find(HttpServletRequest request) {
    try {
      User user = jwtProvider.resolveUser(request);
      return bioRepository.findByUser(user).orElseThrow(
          () -> new CustomException(user.getUsername() + " has not set up his/her bio", HttpStatus.NOT_FOUND));
    } catch (Exception ex) {
      throw new CustomException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
  }
}
