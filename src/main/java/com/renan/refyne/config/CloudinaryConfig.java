package com.renan.refyne.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

  @Value("${cloudinary.url}")
  private String cloudinaryUrl;

  @Bean
  public Cloudinary cloudinary() {
    // Since CLOUDINARY_URL contains all credentials, Cloudinary automatically parses it.
    // We can just construct it with the URL.
    Cloudinary cloudinary = new Cloudinary(cloudinaryUrl);
    cloudinary.config.secure = true;
    return cloudinary;
  }
}
