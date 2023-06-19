package com.t3q.dranswer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "setup")
public class ApplicationProperties {

	private String authUrl;
	private String cmanUrl;
	private String callbackUrl;

}
