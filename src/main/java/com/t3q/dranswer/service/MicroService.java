package com.t3q.dranswer.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

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
import com.t3q.dranswer.dto.cman.CmanContainerDomainCreateDeleteRes;
import com.t3q.dranswer.dto.cman.CmanContainerDomainCreateReq;
import com.t3q.dranswer.dto.cman.CmanImageDeleteRes;
import com.t3q.dranswer.dto.db.DbMicroService;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceCreateRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainMergeRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceListReadRes;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceListReadResSub;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceUpdateRes;
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
	
	private final RestTemplate restTemplate;
	private final ApplicationProperties applicationProperties;
	
	@Autowired
    public MicroService(ApplicationProperties applicationProperties, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }
    
	public ServpotMicroServiceListReadRes readMicroServiceList(String service) {
		log.info("MicroService : readMicroServiceList");
		List<DbMicroService> dbMicroServiceList = new ArrayList<>();
		dbMicroServiceList = microServiceMapper.selectMicroServiceByService(service);
		
		ServpotMicroServiceListReadRes res = new ServpotMicroServiceListReadRes();
		res.setMicroList(new ArrayList<ServpotMicroServiceListReadResSub>());
		res.setServiceId(service);
		for (DbMicroService dbMicroService : dbMicroServiceList) {
			ServpotMicroServiceListReadResSub sub = new ServpotMicroServiceListReadResSub();
			sub.setMicroId(dbMicroService.getMicroService());
			sub.setMicroName(dbMicroService.getMicroServiceName());
			sub.setMicroDomain(dbMicroService.getMicroServiceDomain());
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
		List<String> imageList = new ArrayList<>();
		imageList = microServiceMapper.selectImageByMicro(micro);

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
	
	public ServpotMicroServiceDomainMergeRes createMicroServiceDomain(ServpotMicroServiceDomainMergeReq microReq) throws Exception {
		log.info("MicroService : createMicroServiceDomain");
		ServpotMicroServiceDomainMergeRes res = new ServpotMicroServiceDomainMergeRes();
		res.setMicroId(microReq.getMicroId());
		res.setMicroDomain(microReq.getMicroDomain());
		
		DbMicroService dbMicro = new DbMicroService();
		dbMicro = microServiceMapper.selectMicroService(microReq.getMicroId());
		if (dbMicro == null || dbMicro.getMicroService() == null) {
			throw new Exception(Constants.E40004);
		}
		
		// 마이크로서비스의 이미지(컨테이너) 목록 조회
		List<String> containerList = new ArrayList<>();
		containerList = microServiceMapper.selectContainerByMicro(dbMicro.getMicroService());
		for (String container : containerList) {
			// 이미지(컨테이너) 도메인 삭제 요청
			if (dbMicro.getMicroServiceDomain() != null && dbMicro.getMicroServiceDomain().isEmpty() == false) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> entity = new HttpEntity<>(headers);
				URI uri = UriComponentsBuilder
					    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_DOMAIN_DELETE_URL)
						    .queryParam("projectName", "{projectName}")
					    	.encode()
					    	.buildAndExpand(container, dbMicro.getService())
					    	.toUri();
				try {
					ResponseEntity<CmanContainerDomainCreateDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																										HttpMethod.DELETE, 
																										entity, 
																										CmanContainerDomainCreateDeleteRes.class);
					if (cmanRes.getStatusCode() == HttpStatus.OK) {
						log.info("container domain delete : " + cmanRes.getBody().getMessage());
					}
				} catch (HttpClientErrorException e) {
					e.printStackTrace();
					log.error(e.getMessage());
					//throw new Exception(Constants.E50002);
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
					throw new Exception(Constants.E50000);
				}
			}

			// 이미지(컨테이너) 도메인 생성 요청
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			CmanContainerDomainCreateReq cmanReq = new CmanContainerDomainCreateReq();
			cmanReq.setDomainName(microReq.getMicroDomain());
			cmanReq.setPort(80);		// fix			
			HttpEntity<CmanContainerDomainCreateReq> entity = new HttpEntity<>(cmanReq, headers);
			URI uri = UriComponentsBuilder
				    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_DOMAIN_CREATE_URL)
					    .queryParam("projectName", "{projectName}")
				    	.encode()
				    	.buildAndExpand(container, dbMicro.getService())
				    	.toUri();
			try {
				ResponseEntity<CmanContainerDomainCreateDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																									HttpMethod.POST, 
																									entity, 
																									CmanContainerDomainCreateDeleteRes.class);
				if (cmanRes.getStatusCode() == HttpStatus.OK) {
					log.info("container domain create : " + cmanRes.getBody().getMessage());
				}
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				//throw new Exception(Constants.E50002);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new Exception(Constants.E50000);
			}
		}

		// 마이크로서비스 도메인 변경
		DbMicroService dbMicroService = new DbMicroService();
		dbMicroService.setMicroService(microReq.getMicroId());
		dbMicroService.setMicroServiceDomain(microReq.getMicroDomain());
		microServiceMapper.updateMicroServiceDomain(dbMicroService);

		return res;
	}
	
	public ServpotMicroServiceDomainDeleteRes deleteMicroServiceDomain(String microId) throws Exception {
		log.info("MicroService : deleteMicroServiceDomain");
		DbMicroService dbMicro = new DbMicroService();
		dbMicro = microServiceMapper.selectMicroService(microId);
		List<String> containerList = new ArrayList<>();
		containerList = microServiceMapper.selectContainerByMicro(microId);
		if (dbMicro == null || containerList == null) {
			throw new Exception(Constants.E40004);
		}
		
		ServpotMicroServiceDomainDeleteRes res = new ServpotMicroServiceDomainDeleteRes();
		res.setMicroId(microId);

		for (String container : containerList) {
			// 이미지(컨테이너) 도메인 삭제 요청
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			URI uri = UriComponentsBuilder
				    	.fromUriString(applicationProperties.getCmanUrl() + Constants.CMAN_CONTAINER_DOMAIN_DELETE_URL)
				    	.queryParam("projectName", "{projectName}")
				    	.encode()
				    	.buildAndExpand(container, dbMicro.getService())
				    	.toUri();
			try {
				ResponseEntity<CmanContainerDomainCreateDeleteRes> cmanRes = restTemplate.exchange(	uri, 
																									HttpMethod.DELETE, 
																									entity, 
																									CmanContainerDomainCreateDeleteRes.class);
				if (cmanRes.getStatusCode() == HttpStatus.OK) {
					log.info("container domain delete : " + cmanRes.getBody().getMessage());
				}
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				log.error(e.getMessage());
				//throw new Exception(Constants.E50002);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage());
				throw new Exception(Constants.E50000);
			}
		}
		// 마이크로서비스 도메인 변경
		DbMicroService dbMicroService = new DbMicroService();
		dbMicroService.setMicroService(microId);
		microServiceMapper.updateMicroServiceDomain(dbMicroService);
		return res;
	}
}
