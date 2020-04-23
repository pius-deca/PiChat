package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.ProfilePic;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfilePic, Long> {
  Optional<ProfilePic> findByUser(User user);
  Optional<ProfilePic> findByProfilePic(String pic);
}
