package com.github.pius.pichats.service;

import com.cloudinary.utils.ObjectUtils;
import com.github.pius.pichats.configuration.CloudConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudService {

  private CloudConfiguration cloudConfiguration;
  String fileName = null;

  @Autowired
  public CloudService(CloudConfiguration cloudConfiguration) {
    this.cloudConfiguration = cloudConfiguration;
  }

  public void upload(String path) throws Exception {
    try {
      String num = RandomStringUtils.randomNumeric(5);
      File toUpload = new File(path);
      String folder = "piChat";
      String resource_type = null;
      if (path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".pdf")){
        fileName = "image" + num;
        folder = folder+"/image/";
        resource_type = "image";
      }else if(path.endsWith(".mp4") || path.endsWith(".avi")){
        fileName = "video" + num;
        folder = folder+"/video/";
        resource_type = "video";
      }
      Map params = ObjectUtils.asMap("public_id", folder + fileName,
        "resource_type", resource_type);
      cloudConfiguration.configCloud().uploader().upload(toUpload, params);
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
