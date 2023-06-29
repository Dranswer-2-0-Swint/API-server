package com.t3q.dranswer.dto.servpot;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotAppServiceAllReadResCompany extends ApiResponse {

	private String companyId;										// 기업ID
	private List<ServpotAppServiceListReadResSub> serviceList;		// 응용서비스목록

}
