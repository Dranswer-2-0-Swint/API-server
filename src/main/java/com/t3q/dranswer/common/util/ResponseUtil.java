package com.t3q.dranswer.common.util;

import com.t3q.dranswer.config.Constants;
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
			case Constants.E50001:
				res.setRspMsg(Constants.E50001_MSG);
				break;
			case Constants.E50002:
				res.setRspMsg(Constants.E50002_MSG);
				break;
			default:
				res.setRspCode(Constants.E50000);
				res.setRspMsg(error);
				break;
		}
		
		return res;
	}

}
