package com.t3q.dranswer.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.t3q.dranswer.dto.RequestContext;
import com.t3q.dranswer.dto.servpot.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.t3q.dranswer.common.util.HashUtil;
import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.cman.CmanInitProjectReq;
import com.t3q.dranswer.dto.cman.CmanInitProjectRes;
import com.t3q.dranswer.dto.db.DbAppService;
import com.t3q.dranswer.mapper.AppServiceMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AppService {
	
	@Autowired
	AppServiceMapper appServiceMapper;
	
	private final RestTemplate restTemplate;
	private final ApplicationProperties applicationProperties;

	@Autowired
    public AppService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

	public ServpotAppServiceAllReadRes readAppServiceAll() {
		log.info("AppService : readAppServiceAll");
		List<DbAppService> dbAppServiceList = appServiceMapper.selectServiceAll();

		ServpotAppServiceAllReadRes res = new ServpotAppServiceAllReadRes();
		res.setCompanyList(new ArrayList<>());
		for (DbAppService dbAppService : dbAppServiceList) {
			ServpotAppServiceListReadResSub sub = new ServpotAppServiceListReadResSub();
			sub.setServiceId(dbAppService.getService());
			sub.setServiceName(dbAppService.getServiceName());
//			res.getServiceList().add(sub);
		}

		return res;
	}

	public ServpotAppServiceListReadRes readAppServiceList(String company) {
		log.info("AppService : readAppServiceList");
		List<DbAppService> dbAppServiceList = appServiceMapper.selectServiceByCompany(company);

		ServpotAppServiceListReadRes res = new ServpotAppServiceListReadRes();
		res.setServiceList(new ArrayList<>());
		res.setCompanyId(company);
		for (DbAppService dbAppService : dbAppServiceList) {
			ServpotAppServiceListReadResSub sub = new ServpotAppServiceListReadResSub();
			sub.setServiceId(dbAppService.getService());
			sub.setServiceName(dbAppService.getServiceName());
			res.getServiceList().add(sub);
		}

		return res;
	}

	public ServpotAppServiceCreateRes createAppService(ServpotAppServiceCreateReq serviceReq) {
		log.info("AppService : createAppService");
		String serviceId = Constants.PREFIX_SVC + HashUtil.makeCRC32(appServiceMapper.getServiceSequence());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		log.info("#####this is threadlocal test {}", headers.toString());

		CmanInitProjectReq cmanReq = new CmanInitProjectReq();
		cmanReq.setProjectName(serviceId);
		HttpEntity<CmanInitProjectReq> entity = new HttpEntity<>(cmanReq, headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_PROJECT_CREATE_URL)
			    	.build()
			    	.encode()
			    	.toUri();
		try {
			ResponseEntity<CmanInitProjectRes> cmanRes = restTemplate.exchange(	uri,
																				HttpMethod.POST,
																				entity,
																				CmanInitProjectRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				log.info("message : " + cmanRes.getBody().getMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		}
		DbAppService dbAppService = new DbAppService();
		dbAppService.setService(serviceId);
		dbAppService.setCompany(serviceReq.getCompanyId());
		dbAppService.setServiceName(serviceReq.getServiceName());
		
		appServiceMapper.insertService(dbAppService);
		
		ServpotAppServiceCreateRes res = new ServpotAppServiceCreateRes();
		res.setServiceId(dbAppService.getService());
		res.setCompanyId(dbAppService.getCompany());
		res.setServiceName(dbAppService.getServiceName());
		
		return res;
	}
	
	public ServpotAppServiceDeleteRes deleteService(String service) {
		log.info("AppService : deleteService");
		
		appServiceMapper.deleteService(service);
		
		ServpotAppServiceDeleteRes res = new ServpotAppServiceDeleteRes();
		res.setServiceId(service);
		
		return res;
	}
	
}