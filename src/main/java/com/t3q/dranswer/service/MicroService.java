package com.t3q.dranswer.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.t3q.dranswer.dto.RequestContext;
import com.t3q.dranswer.dto.db.DbMicroDomain;
import com.t3q.dranswer.dto.servpot.*;
import com.t3q.dranswer.mapper.MicroDomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.t3q.dranswer.common.util.HashUtil;
import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.cman.CmanImageDeleteRes;
import com.t3q.dranswer.dto.db.DbMicroService;
import com.t3q.dranswer.mapper.ImageMapper;
import com.t3q.dranswer.mapper.MicroServiceMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MicroService {
	
	@Autowired
	ImageMapper imageMapper;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	MicroServiceMapper microServiceMapper;

	@Autowired
	MicroDomainMapper microDomainMapper;
	
	private final RestTemplate restTemplate;
	private final ApplicationProperties applicationProperties;
	
	@Autowired
    public MicroService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }
    
	public ServpotMicroServiceListReadRes readMicroServiceList(String service) {

		log.info("MicroService : readMicroServiceList");
		List<DbMicroService> dbMicroServiceList = microServiceMapper.selectMicroServiceByService(service);
		ServpotMicroServiceListReadRes res = new ServpotMicroServiceListReadRes();
		res.setMicroList(new ArrayList<>());
		res.setServiceId(service);

		for (DbMicroService dbMicroService : dbMicroServiceList) {
			ServpotMicroServiceListReadResSub sub = new ServpotMicroServiceListReadResSub();
			sub.setMicroId(dbMicroService.getMicroService());
			sub.setMicroName(dbMicroService.getMicroServiceName());


			res.getMicroList().add(sub);
		}

		return res;
	}


	
	public ServpotMicroServiceCreateRes createMicroService(ServpotMicroServiceCreateReq microReq) {
		log.info("MicroService : createMicroService");
		String microId = Constants.PREFIX_MIC + HashUtil.makeCRC32(microServiceMapper.getMicroSequence());

		DbMicroService dbMicroService = new DbMicroService();
		dbMicroService.setMicroService(microId);
		dbMicroService.setService(microReq.getServiceId());
		dbMicroService.setMicroServiceName(microReq.getMicroName());
		
		microServiceMapper.insertMicroService(dbMicroService);
		
		ServpotMicroServiceCreateRes res = new ServpotMicroServiceCreateRes();
		res.setServiceId(dbMicroService.getService());
		res.setMicroId(dbMicroService.getMicroService());
		res.setMicroName(dbMicroService.getMicroServiceName());

		return res;
	}


	
	public ServpotMicroServiceUpdateRes updateMicroService(ServpotMicroServiceUpdateReq microReq) {
		log.info("MicroService : updateMicroService");
		DbMicroService dbMicroService = new DbMicroService();
		dbMicroService.setMicroService(microReq.getMicroId());
		dbMicroService.setMicroServiceName(microReq.getMicroName());
		
		microServiceMapper.updateMicroServiceName(dbMicroService);
		
		ServpotMicroServiceUpdateRes res = new ServpotMicroServiceUpdateRes();
		res.setMicroId(dbMicroService.getMicroService());
		res.setMicroName(dbMicroService.getMicroServiceName());
		
		return res;
	}
	
	public ServpotMicroServiceDeleteRes deleteMicroService(String micro) throws Exception {
		log.info("MicroService : deleteMicroService");
		List<String> imageList = microServiceMapper.selectImageByMicro(micro);

		for (String image : imageList) {
			try {
				imageService.deleteImage(image);
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new Exception(Constants.E50002);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new Exception(Constants.E50000);
			}
		}
		
		String service = microServiceMapper.selectServiceByMicro(micro);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RequestContext.RequestContextData localdata = RequestContext.getContextData();
		headers.add("request_id", localdata.getRequestId());
		headers.add("access_token", localdata.getAccessToken());
		HttpEntity<String> entity = new HttpEntity<>(headers);
		URI uri = UriComponentsBuilder
			    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_IMAGE_DELETE_URL)
				    .queryParam("projectName", "{projectName}")
				    .encode()
				    .buildAndExpand(micro, service)
				    .toUri();
		try {
			ResponseEntity<CmanImageDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																				HttpMethod.DELETE, 
																				entity, 
																				CmanImageDeleteRes.class);
			if (cmanRes.getStatusCode() == HttpStatus.OK) {
				log.info("container deploy success.\nenvName : " + cmanRes.getBody().getMessage());
				microServiceMapper.deleteMicroService(micro);
			}
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50002);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			throw new Exception(Constants.E50000);
		}
		
		ServpotMicroServiceDeleteRes res = new ServpotMicroServiceDeleteRes();
		res.setMicroId(micro);

		return res;
	}
	//TODO 수정해야됌 ServpotMicroServiceDomainPostReq으로 변경후 mapper 사용할 것 V
	public ServpotMicroServiceDomainPostRes createMicroServiceDomain(ServpotMicroServiceDomainPostReq microReq) {
		log.info("MicroService : createMicroServiceDomain");

		DbMicroDomain dbMicroDomain = new DbMicroDomain();

		dbMicroDomain.setMicroService(microReq.getMicroId());
		dbMicroDomain.setDomain(microReq.getMicroDomain());
		dbMicroDomain.setPath(microReq.getMicroDomainPath());
		dbMicroDomain.setPort(microReq.getMicroDomainPort());

		microDomainMapper.insertMicroDomain(dbMicroDomain);

		ServpotMicroServiceDomainPostRes res = new ServpotMicroServiceDomainPostRes();
		res.setMicroId(microReq.getMicroId());
		res.setMicroDomain(microReq.getMicroDomain());
		res.setMicroPath(microReq.getMicroDomainPath());
		res.setMicroPort(microReq.getMicroDomainPort());

		return res;
	}


	//TODO 마이크로서비스 도메인 설정 후 돌려줄 RES 만들기 V
	public ServpotMicroServiceDomainPutRes updateMicroServiceDomain(ServpotMicroServiceDomainPutReq microReq){

		DbMicroDomain dbMicroDomain = new DbMicroDomain();

		dbMicroDomain.setMicroService(microReq.getMicroId());
		dbMicroDomain.setDomain(microReq.getMicroDomain());
		dbMicroDomain.setPath(microReq.getMicroDomainPath());
		dbMicroDomain.setPort(microReq.getMicroDomainPort());

		microDomainMapper.updateMicroDomain(dbMicroDomain);

		ServpotMicroServiceDomainPutRes res = new ServpotMicroServiceDomainPutRes();
		res.setMicroId(microReq.getMicroId());
		res.setMicroDomain(microReq.getMicroDomain());
		res.setMicroPath(microReq.getMicroDomainPath());
		res.setMicroPort(microReq.getMicroDomainPort());

		return res;
	}


	public ServpotMicroServiceDomainDeleteRes deleteMicroServiceDomain(String microId) {
		log.info("MicroService : deleteMicroServiceDomain");

		DbMicroService dbMicroService = new DbMicroService();
		dbMicroService.setMicroService(microId);
		microServiceMapper.updateMicroServiceDomain(dbMicroService);
		
		ServpotMicroServiceDomainDeleteRes res = new ServpotMicroServiceDomainDeleteRes();
		res.setMicroId(microId);

		return res;
	}
	
}
