package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.BioDTO;
import com.github.pius.pichats.dto.ChangePasswordDTO;
import com.github.pius.pichats.dto.UpdateRequestDTO;
import com.github.pius.pichats.dto.UpdateResponseDTO;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
  User searchByUsername(String username, HttpServletRequest request);
  List<User> searchUsernameByString(String username, HttpServletRequest request);
  String changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request);
  UpdateResponseDTO updateUser(UpdateRequestDTO updateRequestDTO, HttpServletRequest request);
  Bio updateUserBio(BioDTO bioDTO, HttpServletRequest request);
}
