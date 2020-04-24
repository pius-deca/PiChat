package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.BioDTO;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.service.BioService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping
public class BioController {
  private BioService bioService;
  private ModelMapper modelMapper;

  public BioController(BioService bioService, ModelMapper modelMapper) {
    this.bioService = bioService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/user/bio")
  public ResponseEntity<ApiResponse<Bio>> updateProfile(@Valid @RequestBody BioDTO bio, HttpServletRequest request){
    Bio newBio = bioService.addOrUpdate(modelMapper.map(bio, Bio.class), request);
    ApiResponse<Bio> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newBio);
    response.setMessage("A user has updated is bio");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}
