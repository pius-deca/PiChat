package com.github.pius.pichats.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("com.cloudinary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloudConfiguration {
  private String cloudName;
  private String apiKey;
  private String apiSecret;

  public Cloudinary configCloud(){
    return new Cloudinary(ObjectUtils.asMap(
      "cloud_name", cloudName,
      "api_key", apiKey,
      "api_secret", apiSecret));
  }

}
