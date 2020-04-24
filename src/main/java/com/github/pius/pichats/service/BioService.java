package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Bio;

import javax.servlet.http.HttpServletRequest;

public interface BioService {
  Bio addOrUpdate(Bio bio, HttpServletRequest request);
}
