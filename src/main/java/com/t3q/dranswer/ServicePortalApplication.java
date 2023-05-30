package com.t3q.dranswer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.t3q.dranswer.config.ApplicationProperties;

@SpringBootApplication
@ComponentScan("com.t3q")
@EnableConfigurationProperties(ApplicationProperties.class)
public class ServicePortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServicePortalApplication.class, args);
	}

}
