package com.github.pius.pichats.service.implementation;

import com.cloudinary.utils.ObjectUtils;
import com.github.pius.pichats.configuration.CloudConfiguration;
import com.github.pius.pichats.exceptions.CustomException;
import com.github.pius.pichats.service.ImageFolder;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

  private final CloudConfiguration cloudConfiguration;
  private final ImageFolder imageFolder;
  private String fileName;

  @Autowired
  public CloudService(CloudConfiguration cloudConfiguration, ImageFolder imageFolder) {
    this.cloudConfiguration = cloudConfiguration;
    this.imageFolder = imageFolder;
  }

  public Map upload(MultipartFile file) throws Exception {
    try {
      String num = RandomStringUtils.randomNumeric(5);
      Path filepath = Paths.get(Objects.requireNonNull(file.getOriginalFilename()));
      try(OutputStream os = Files.newOutputStream(filepath)){
        os.write(file.getBytes());
        File toUpload = filepath.toFile();
        String folder = "piChat";
        String resource_type = null;
        if (imageFormat(filepath)){
          setFileName("image"+num);
          folder = folder+"/image/";
          resource_type = "image";
        }else if(videoFormat(filepath)){
          setFileName("video"+num);
          folder = folder+"/video/";
          resource_type = "video";
        }
        Map params = ObjectUtils.asMap("public_id", folder + getFileName(),
          "resource_type", resource_type);
        Map out = cloudConfiguration.configCloud().uploader().upload(toUpload, params);
        return out;
      }catch (IOException ex){
        throw new Exception(ex.getMessage());
      }
    }catch (IOException ex){
      throw new Exception(ex.getMessage());
    }
  }

  public static boolean imageFormat(Path filePath){
    if (filePath.toString().toLowerCase().endsWith(".jpg") || filePath.toString().toLowerCase().endsWith(".jpeg") || filePath.toString().toLowerCase().endsWith(".png") || filePath.toString().toLowerCase().endsWith(".pdf")){
      return true;
    }
    throw new CustomException("Not an image format", HttpStatus.BAD_REQUEST);
  }

  public static boolean videoFormat(Path filePath){
    if (filePath.toString().toLowerCase().endsWith(".mp4") || filePath.toString().toLowerCase().endsWith(".mpeg") || filePath.toString().toLowerCase().endsWith(".avi") || filePath.toString().toLowerCase().endsWith(".mkv")){
      return true;
    }
    throw new CustomException("Not a video format", HttpStatus.BAD_REQUEST);
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
