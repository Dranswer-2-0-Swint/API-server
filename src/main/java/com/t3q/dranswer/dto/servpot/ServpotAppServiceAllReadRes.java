package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotAppServiceAllReadRes extends ApiResponse {

	private String requestId;											// 요청ID
	private List<ServpotAppServiceAllReadResCompany> companyList;		// 기업목록

}
