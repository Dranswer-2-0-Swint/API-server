package com.t3q.dranswer.service;

import java.util.ArrayList;
import java.util.List;

import com.t3q.dranswer.dto.db.DbMicroDomain;
import com.t3q.dranswer.dto.servpot.*;
import com.t3q.dranswer.mapper.MicroDomainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.t3q.dranswer.common.util.HashUtil;
import com.t3q.dranswer.config.ApplicationProperties;
import com.t3q.dranswer.config.Constants;
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
			ServpotMicroServiceListReadResMicro sub = new ServpotMicroServiceListReadResMicro();
			sub.setMicroId(dbMicroService.getMicroService());
			sub.setMicroName(dbMicroService.getMicroServiceName());
			sub.setDomains(new ArrayList<>());
			List<DbMicroDomain> dbMicroDomainList = microDomainMapper.selectMicroDomainByMicro(dbMicroService.getMicroService());
			for (DbMicroDomain dbMicroDomain : dbMicroDomainList) {
				ServpotMicroServiceListReadResMicroDomain microDomain = new ServpotMicroServiceListReadResMicroDomain();
				microDomain.setDomain(dbMicroDomain.getDomain());
				microDomain.setPath(dbMicroDomain.getPath());
				microDomain.setPort(dbMicroDomain.getPort());
				sub.getDomains().add(microDomain);
			}
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

		microDomainMapper.deleteMicroDomainByMicro(micro);
		microServiceMapper.deleteMicroService(micro);

		ServpotMicroServiceDeleteRes res = new ServpotMicroServiceDeleteRes();
		res.setMicroId(micro);

		return res;
	}

	public ServpotMicroServiceDomainCreateRes createMicroServiceDomain(ServpotMicroServiceDomainCreateReq microReq) throws Exception {
		log.info("MicroService : createMicroServiceDomain");
		if (microDomainMapper.selectMicroDomain(microReq.getDomain()) != null) {
			throw new Exception(Constants.E40005);
		} else if (microDomainMapper.selectMicroDomainPort(microReq.getMicroId(), String.valueOf(microReq.getPort())) != null) {
			throw new Exception(Constants.E40006);
		}

		DbMicroDomain dbMicroDomain = new DbMicroDomain();

		dbMicroDomain.setMicroService(microReq.getMicroId());
		dbMicroDomain.setDomain(microReq.getDomain());
		dbMicroDomain.setPath(microReq.getPath());
		dbMicroDomain.setPort(microReq.getPort());

		microDomainMapper.insertMicroDomain(dbMicroDomain);

		ServpotMicroServiceDomainCreateRes res = new ServpotMicroServiceDomainCreateRes();
		res.setMicroId(microReq.getMicroId());
		res.setDomain(microReq.getDomain());
		res.setPath(microReq.getPath());
		res.setPort(microReq.getPort());

		return res;
	}

	public ServpotMicroServiceDomainUpdateRes updateMicroServiceDomain(ServpotMicroServiceDomainUpdateReq microReq){
		log.info("MicroService : updateMicroServiceDomain");
		if (microDomainMapper.selectMicroDomain(microReq.getRenewDomain()) != null) {
			new Exception(Constants.E40005);
		}

		List<DbMicroDomain> microDomains = microDomainMapper.selectMicroDomainByMicro(microReq.getMicroId());
		for(DbMicroDomain microDomain: microDomains) {
			if( microDomain.getDomain().equals(microReq.getPresentDomain())){
				microDomainMapper.updateMicroDomain(microReq);
				break;
			}
		}

		ServpotMicroServiceDomainUpdateRes res = new ServpotMicroServiceDomainUpdateRes();
		res.setMicroId(microReq.getMicroId());
		res.setDomain(microReq.getRenewDomain());
		res.setPath(microReq.getPath());
		res.setPort(microReq.getPort());

		return res;
	}

	public ServpotMicroServiceDomainDeleteRes deleteMicroServiceDomain(ServpotMicroServiceDomainDeleteReq microReq) {
		log.info("MicroService : deleteMicroServiceDomain");

		DbMicroService dbMicroService = new DbMicroService();
		dbMicroService.setMicroService(microReq.getMicroId());
		microDomainMapper.deleteMicroDomain(microReq);

		ServpotMicroServiceDomainDeleteRes res = new ServpotMicroServiceDomainDeleteRes();
		res.setMicroId(microReq.getMicroId());

		return res;
	}

}
