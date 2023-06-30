package com.t3q.dranswer.dto.servpot;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotAppServiceReadMicroServicesAndImagesRes {

    private String requestId;										    // 요청ID
    private String companyId;										    // 기업ID
    private String serviceId;										    // 서비스ID
    private
    List<ServpotAppServiceReadMicroServicesAndImagesResSub> microsWithImages ;    // 이미지 목록을 포함한 마이크로 서비스들
}
