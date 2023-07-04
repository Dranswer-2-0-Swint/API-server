package com.t3q.dranswer.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3q.dranswer.common.util.HashUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.t3q.dranswer.dto.RequestContext;
import com.t3q.dranswer.entity.LogEntity;
import com.t3q.dranswer.repository.LoggingRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {

    private final LoggingRepository loggingRepository;

    public LoggingFilter(LoggingRepository loggingRepository) {
        this.loggingRepository = loggingRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        String Headers = getHeaders(requestWrapper).toString();
        String queryString = getQueryParameter(requestWrapper).toString();
        String RequestBody = contentBody(requestWrapper.getContentAsByteArray());

        log.info("\n=======URI: [{}], METHOD: [{}]=======\n", request.getRequestURI(), request.getMethod());
        log.info("\nHeaders: {}\nQueryString: {}\nRequest Body: {}\n", Headers, queryString, RequestBody);

        LogEntity logEntity = new LogEntity();
        String requestId = request.getHeader("request_id");
        logEntity.setReq_id(requestId);
        RequestContext.RequestContextData localdata = RequestContext.getContextData();
        logEntity.setReq_user(localdata == null? "": localdata.getReq_user());
        logEntity.setReq_prm(requestWrapper.getQueryString());
        logEntity.setReq_body(RequestBody);
        logEntity.setReq_dt(LocalDateTime.now());
        logEntity.setReq_uri(requestWrapper.getRequestURI());
        logEntity.setReq_md(requestWrapper.getMethod());

        loggingRepository.save(logEntity);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String ResponseBody = contentBody(responseWrapper.getContentAsByteArray());
        ObjectMapper resobjectMapper = new ObjectMapper();
        JsonNode resNode = resobjectMapper.readTree(ResponseBody);

        log.info("\nResponse Body: {}\n", ResponseBody);

        logEntity.setRes_user("swint");
        logEntity.setRes_body(ResponseBody);
        logEntity.setRes_dt(LocalDateTime.now());
        logEntity.setRes_msg(String.valueOf(resNode.get("message")));
        logEntity.setRes_status(responseWrapper.getStatus());

        loggingRepository.save(logEntity);
        responseWrapper.copyBodyToResponse();
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headerMap = new HashMap<>();

        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private Map<String, String> getQueryParameter(HttpServletRequest request) {
        Map<String, String> queryMap = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> queryMap.put(key, String.join("", value)));
        return queryMap;
    }

    private String contentBody(final byte[] contents) {
        StringBuilder sb = new StringBuilder("\n");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(contents)));
        bufferedReader.lines().forEach(str -> sb.append(str).append("\n"));
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
