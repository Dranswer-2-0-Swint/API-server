package com.t3q.dranswer.filter;

import com.t3q.dranswer.entity.LogEntity;
import com.t3q.dranswer.repository.LoggingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
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
        LogEntity logEntity = new LogEntity();
        filterChain.doFilter(requestWrapper, responseWrapper);

        String Headers = getHeaders(requestWrapper).toString();
        String queryString = getQueryParameter(requestWrapper).toString();
        String RequestBody = contentBody(requestWrapper.getContentAsByteArray());
        String ResponseBody = contentBody(responseWrapper.getContentAsByteArray());

        log.info("=======URI: [{}], METHOD: [{}]=======", request.getRequestURI(), request.getMethod());
        log.info("Headers: {}", Headers);
        log.info("QueryString: {}", queryString);
        log.info("Request Body: {}", RequestBody);
        log.info("Response Body: {}", ResponseBody);

        ///jpa
        //logEntity.setReq_id(requestWrapper.getParameter("request_id"));
        logEntity.setReq_id("req_id test");
        logEntity.setReq_user("servpot");
        logEntity.setReq_body(RequestBody);
        logEntity.setReq_dt(LocalDateTime.now());
        logEntity.setReq_uri(requestWrapper.getRequestURI());
        logEntity.setReq_md(requestWrapper.getMethod());
        logEntity.setRes_user("swint");
        logEntity.setRes_body(ResponseBody);
        logEntity.setRes_dt(LocalDateTime.now());
        //logEntity.setRes_msg(request.getParameter("message"));
        logEntity.setRes_msg("msg test");
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
        request.getParameterMap()
                .forEach((key, value) -> queryMap.put(key, String.join("", value)));
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
