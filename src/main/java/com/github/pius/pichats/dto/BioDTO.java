package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BioDTO {
  private String description;
  private String phone;
  private Date dob;
  private String address;
  private String country;
}
