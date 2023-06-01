package com.t3q.dranswer.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

	@Override
	public boolean hasError(ClientHttpResponse response) throws IOException {
		// TODO Auto-generated method stub
		return (
				response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
				response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR
				);
	}

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {
		// TODO Auto-generated method stub
		String seriesName = "UNKNOWN_ERROR";
		
		if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
			seriesName = HttpStatus.Series.SERVER_ERROR.name();
		} else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
			seriesName = HttpStatus.Series.CLIENT_ERROR.name();
			if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
				seriesName = HttpStatus.NOT_FOUND.name();
			}
		}
	}
}
