package com.github.pius.pichats.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;

@Configuration
public class ApiConfiguration {

  @Value("${spring.mail.properties.mail.smtp.username}")
  public String USERNAME;
  @Value("${spring.mail.properties.mail.smtp.password}")
  public String PASSWORD;

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailMessage = new JavaMailSenderImpl();
    mailMessage.setHost("smtp.gmail.com");
    mailMessage.setPort(587);
    mailMessage.setUsername(USERNAME);
    mailMessage.setPassword(PASSWORD);

    Properties properties = mailMessage.getJavaMailProperties();
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.auth", "true");
    properties.put("mail.smtp.starttls.enable", "true");

    return mailMessage;
  }
}
