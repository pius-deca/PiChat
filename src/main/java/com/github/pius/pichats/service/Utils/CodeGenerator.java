package com.github.pius.pichats.service.Utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class CodeGenerator {

  public String activationToken(){
    return RandomStringUtils.randomAlphanumeric(6);
  }

}
