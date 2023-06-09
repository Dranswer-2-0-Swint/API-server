package com.t3q.dranswer.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.dto.cman.ErrorResponse;
import com.t3q.dranswer.dto.servpot.ApiResponse;

public class ResponseUtil {

	public static ApiResponse parseRspCode(String error) {
		ApiResponse res = new ApiResponse();
		
		res.setRspCode(error);
		switch (error) {
			case Constants.E00000:
				res.setRspMsg(Constants.E00000_MSG);
				break;
			case Constants.E40001:
				res.setRspMsg(Constants.E40001_MSG);
				break;
			case Constants.E40002:
				res.setRspMsg(Constants.E40002_MSG);
				break;
			case Constants.E40003:
				res.setRspMsg(Constants.E40003_MSG);
				break;
			case Constants.E40004:
				res.setRspMsg(Constants.E40004_MSG);
				break;
			case Constants.E40005:
				res.setRspMsg(Constants.E40005_MSG);
				break;
			case Constants.E40006:
				res.setRspMsg(Constants.E40006_MSG);
				break;
			case Constants.E40007:
				res.setRspMsg(Constants.E40007_MSG);
				break;
			case Constants.E50001:
				res.setRspMsg(Constants.E50001_MSG);
				break;
			case Constants.E50002:
				res.setRspMsg(Constants.E50002_MSG);
				break;
			case Constants.E50003:
				res.setRspMsg(Constants.E50003_MSG);
				break;
			default:
				res.setRspCode(Constants.E50000);
				res.setRspMsg(error);
				break;
		}
		
		return res;
	}
	
	public static ErrorResponse parseJsonString(String jsonString) throws JsonMappingException, JsonProcessingException {

		ObjectMapper mapper = new ObjectMapper();
		ErrorResponse res = mapper.readValue(jsonString, ErrorResponse.class);
		
		return res;
	}
	

	public static String getStatusCode(String detail) {
		String status = new String();
		
		switch(detail) {
			// 동작중
			case Constants.DETAIL_RUNNING :
				status = Constants.STATUS_POD_RUNNING;
				break;
			// 시동중
			case Constants.DETAIL_UPLOADING :
				status = Constants.STATUS_IMAGE_UPLOADING;
				break;
			// 종료중
			case Constants.DETAIL_DEPLOYING :
				status = Constants.STATUS_POD_DEPLOYING;
				break;
			// 종료중
			case Constants.DETAIL_TERMINATING :
				status = Constants.STATUS_POD_TERMINATING;
				break;
			// 중지
			case Constants.DETAIL_SHUTDOWN :
				status = Constants.STATUS_POD_STOPPED;
				break;
			// 이미지 업로드 실패
			case Constants.DETAIL_ERROR_LOAD :
			case Constants.DETAIL_ERROR_PUSH :
				status = Constants.STATUS_IMAGE_UPLOAD_FAILED;
				break;
			// 컨테이너 실행 실패
			case Constants.DETAIL_ERROR_IMAGE_PULL :
			case Constants.DETAIL_ERROR_OOM :
			case Constants.DETAIL_ERROR_PENDING :
			case Constants.DETAIL_ERROR_CRASHED :
			case Constants.DETAIL_ERROR_INTERNAL_ERR :
				status = Constants.STATUS_DEPLOY_FAILED;
				break;
			default :
				break;
			
		}

		return status;
	}

}
