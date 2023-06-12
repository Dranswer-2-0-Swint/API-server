package com.t3q.dranswer.config;

import java.time.Duration;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.t3q.dranswer.interceptor.LoggingInterceptor;
import com.t3q.dranswer.repository.LoggingRepository;

@Configuration
public class RestTemplateConfig {
	@Bean
	HttpClient httpClient() {
		return HttpClientBuilder.create()
				.setMaxConnTotal(100)
				.setMaxConnPerRoute(20)
				.build();
	}

	@Bean
	HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setReadTimeout(60000);
		factory.setConnectTimeout(60000);
		factory.setHttpClient(httpClient);

		return factory;
	}

	@Bean
	RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory, LoggingRepository loggingRepository) {
//		RestTemplate restTemplate = new RestTemplate(factory);
//		restTemplate.getInterceptors().add(new LoggingInterceptor());
//		return restTemplate;
		return new RestTemplateBuilder()
				.setConnectTimeout(Duration.ofSeconds(60))
				.setReadTimeout(Duration.ofSeconds(60))
				.additionalInterceptors(new LoggingInterceptor(loggingRepository))
				.requestFactory(() -> new BufferingClientHttpRequestFactory(factory))
				.build();
	}

}
