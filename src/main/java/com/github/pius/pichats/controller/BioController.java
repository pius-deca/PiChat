package com.github.pius.pichats.controller;

import com.github.pius.pichats.apiresponse.ApiResponse;
import com.github.pius.pichats.dto.*;
import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("")
@CrossOrigin
public class BioController {

  private BioService bioService;
  private ModelMapper modelMapper;

  @Autowired
  public BioController(BioService bioService, ModelMapper modelMapper){
    this.bioService = bioService;
    this.modelMapper = modelMapper;
  }

  @PostMapping("/user/bio")
  public ResponseEntity<ApiResponse<Bio>> addBio(@Valid @RequestBody BioDTO bio, HttpServletRequest request){
    Bio newBio = bioService.addOrUpdate(modelMapper.map(bio, Bio.class), request);
    ApiResponse<Bio> response = new ApiResponse<>(HttpStatus.CREATED);
    response.setData(newBio);
    response.setMessage("A user has updated is bio");
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @GetMapping("/user/bio")
  public ResponseEntity<ApiResponse<Bio>> getBio(HttpServletRequest request){
    Bio foundBio = bioService.find(request);
    ApiResponse<Bio> response = new ApiResponse<>(HttpStatus.OK);
    response.setData(foundBio);
    response.setMessage("User's bio retrieved successfully");
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
