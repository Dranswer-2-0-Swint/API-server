package com.t3q.dranswer.dto.servpot;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ServpotAppServiceReadMicroServicesAndImagesResSub {
    private String microId;										// 마이크로서비스ID
    private List<ServpotImageListReadResSub> imageList;			// 이미지목록


}
