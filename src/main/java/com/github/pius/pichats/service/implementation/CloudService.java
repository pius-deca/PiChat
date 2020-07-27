package com.github.pius.pichats.service.implementation;

import com.cloudinary.utils.ObjectUtils;
import com.github.pius.pichats.configuration.CloudConfiguration;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;

@Data
@Service
public class CloudService {

  private CloudConfiguration cloudConfiguration;
  private String fileName;

  @Autowired
  public CloudService(CloudConfiguration cloudConfiguration) {
    this.cloudConfiguration = cloudConfiguration;
  }

  public Object upload(MultipartFile file) throws Exception {
    try {
      String num = RandomStringUtils.randomNumeric(5);
      Path filepath = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
      try(OutputStream os = Files.newOutputStream(filepath)){
        os.write(file.getBytes());
        File toUpload = filepath.toFile();
        String folder = "piChat";
        String resource_type = null;
        if (filepath.toString().endsWith(".jpg") || filepath.toString().endsWith(".png") || filepath.toString().endsWith(".pdf")
          || filepath.toString().endsWith(".JPG") || filepath.toString().endsWith(".PNG") || filepath.toString().endsWith(".PNG") || filepath.toString().endsWith(".jpeg") || filepath.toString().endsWith(".JPEG")){
          setFileName("image"+num);
          folder = folder+"/image/";
          resource_type = "image";
        }else if(filepath.toString().endsWith(".mp4") || filepath.toString().endsWith(".avi")){
          setFileName("video"+num);
          folder = folder+"/video/";
          resource_type = "video";
        }
        Map params = ObjectUtils.asMap("public_id", folder + getFileName(),
          "resource_type", resource_type);
        Map out = cloudConfiguration.configCloud().uploader().upload(toUpload, params);
        return out.get("secure_url");
      }catch (IOException ex){
        throw new Exception(ex.getMessage());
      }
    }catch (IOException ex){
      throw new Exception(ex.getMessage());
    }
  }

  public void deleteFile(String post) throws Exception {
    try {
      String publicId = null;
      String folder = "piChat";
      String resource_type = null;
      if (post.startsWith("image")){
        folder = folder+"/image/";
        publicId = folder+post;
        resource_type = "image";
      }else if(post.startsWith("video")){
        folder = folder+"/video/";
        publicId = folder+post;
        resource_type = "video";
      }
      Map params = ObjectUtils.asMap("public_id", folder + post,
        "resource_type", resource_type);
      cloudConfiguration.configCloud().uploader().destroy(publicId, params);
    }catch (IOException ex){
      throw new Exception(ex.getMessage());
    }
  }
}
