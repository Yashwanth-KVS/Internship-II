package com.example.talent_api;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyCorsConfiguration implements WebMvcConfigurer {

	  @Override
	  public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	      .allowedOrigins("http://localhost:5173")
	      .allowedMethods("*")
	      .allowedHeaders("Content-Type", "Authorization")
	      .allowCredentials(true);
	  }
}