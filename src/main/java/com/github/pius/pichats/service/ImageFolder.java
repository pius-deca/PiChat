package com.github.pius.pichats.service;

import com.github.pius.pichats.exceptions.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Objects;

@Service
public class ImageFolder {
  public void createResourceFolder(String dir, MultipartFile file) throws IOException {
    boolean success = false;
    File directory = new File(dir);
    if (!directory.exists()) {
      success = directory.mkdir();
    }
    if (success){
      System.out.println("created directory! "+success);
    }else {
      System.out.println("Directory already exists, please proceed!! "+success);
    }

    File fileName = new File(directory, Objects.requireNonNull(file.getOriginalFilename()));
    try(OutputStream os = Files.newOutputStream(fileName.toPath())){
      os.write(file.getBytes());
      if (!fileName.exists()) {
        success = fileName.createNewFile();
      }
      if (success) {
        System.out.println("created file! "+success);
      }else {
        System.out.println("file already exists! "+success);
      }
    }catch (IOException e){
      throw new CustomException(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
