package com.t3q.dranswer.interceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3q.dranswer.entity.LogEntity;
import com.t3q.dranswer.repository.LoggingRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


@Log4j2
@Slf4j
@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {
    private final LoggingRepository loggingRepository;

    public LoggingInterceptor(LoggingRepository loggingRepository) {
        this.loggingRepository = loggingRepository;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {

        LogEntity logEntity = new LogEntity();

        String requestBody = new String(body, StandardCharsets.UTF_8);
        ObjectMapper reqobjectMapper = new ObjectMapper();
        JsonNode reqNode = reqobjectMapper.readTree(requestBody);




        // Log request data before sending the request
        logRequest(request, body);





        // Execute the request
        ClientHttpResponse response = execution.execute(request, body);




        String responseBody = getResponseBody(response);
        ObjectMapper resobjectMapper = new ObjectMapper();
        JsonNode resNode = resobjectMapper.readTree(responseBody);

        // Log response data after receiving the response
        logResponse(response);

        String req_id = request.getHeaders().getFirst("request_id");
        logEntity.setReq_id(req_id);
        logEntity.setReq_user("swint");
        logEntity.setReq_body(requestBody);
        logEntity.setReq_dt(LocalDateTime.now());
        logEntity.setReq_uri(String.valueOf(request.getURI()));
        logEntity.setReq_md(request.getMethodValue());
        logEntity.setQry_prm(request.getURI().getQuery());

        //TODO keycloak인지 cman인지?

        logEntity.setRes_user("cman");
        logEntity.setRes_body(responseBody);
        logEntity.setRes_dt(LocalDateTime.now());
        logEntity.setRes_msg(String.valueOf(resNode.get("message")));
        logEntity.setRes_status(response.getRawStatusCode());

        loggingRepository.save(logEntity);

        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        // Extract relevant request data and save it to the database
        String url = request.getURI().toString();
        String requestBody = new String(body);  // Modify as per your requirements
        log.info("########hihihihihihi this is request url !!!! {}",url);
        log.info("this is bodybody {}", requestBody);
        // Save request data to the database
        // ...
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        // Extract relevant response data and save it to the database
        HttpStatus statusCode = response.getStatusCode();
        String responseBody = getResponseBody(response);
        //json parsing
        log.info("########hihihihihihi this is response SC!!!!! {}",statusCode);
        log.info("this is bodybody {}", responseBody);

    }

    private String getResponseBody(ClientHttpResponse response) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()));
        StringBuilder responseBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseBody.append(line);
        }
        return responseBody.toString();
    }
}


