package com.t3q.dranswer.dto.cman;

import lombok.Data;

import java.util.List;

@Data
public class CmanContainerDomainCreateReq {

	private boolean	 hasDomain;
	private List<CmanContainerDomain> domainList;
}

