package com.github.pius.pichats.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.github.pius.pichats.dto.ChangePasswordDTO;
import com.github.pius.pichats.dto.UpdateRequestDTO;
import com.github.pius.pichats.dto.UpdateResponseDTO;
import com.github.pius.pichats.model.User;

public interface UserService {
  User searchByUsername(String username, HttpServletRequest request);

  List<User> searchUsernameByString(String username, HttpServletRequest request);

  String changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request);

  UpdateResponseDTO updateUser(UpdateRequestDTO updateRequestDTO, HttpServletRequest request);
  // Bio updateUserBio(BioDTO bioDTO, HttpServletRequest request);
}
