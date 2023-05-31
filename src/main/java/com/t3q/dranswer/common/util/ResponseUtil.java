package com.t3q.dranswer.common.util;

import com.t3q.dranswer.config.Constants;
import com.t3q.dranswer.config.ConstantsToValidation;
import com.t3q.dranswer.dto.servpot.ApiResponse;

public class ResponseUtil {

	public static ApiResponse parseRspCode(String error) {
		ApiResponse res = new ApiResponse();
		
		res.setRspCode(error);
		switch (error) {
			case ConstantsToValidation.E00000:
				res.setRspMsg(ConstantsToValidation.E00000_MSG);
				break;
			case ConstantsToValidation.E40001:
				res.setRspMsg(ConstantsToValidation.E40001_MSG);
				break;
			case ConstantsToValidation.E40002:
				res.setRspMsg(ConstantsToValidation.E40002_MSG);
				break;
			case ConstantsToValidation.E40003:
				res.setRspMsg(ConstantsToValidation.E40003_MSG);
				break;
			case ConstantsToValidation.E50001:
				res.setRspMsg(ConstantsToValidation.E50001_MSG);
				break;
			case ConstantsToValidation.E50002:
				res.setRspMsg(ConstantsToValidation.E50002_MSG);
				break;
			default:
				res.setRspCode(ConstantsToValidation.E50000);
				res.setRspMsg(error);
				break;
		}
		
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
