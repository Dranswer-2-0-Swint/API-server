package com.t3q.dranswer.common.util;

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

}
