package com.t3q.dranswer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.t3q.dranswer.dto.db.DbAppService;

@Mapper
@Repository
public interface AppServiceMapper {
	// 기업의 응용서비스 순번
	public int getServiceSequence();
	// 응용서비스 조회
	public DbAppService selectAppServiceByService(@Param("service") String service);
	// 응용서비스 목록 조회
	public List<DbAppService> selectAppServiceByCompany(@Param("company") String company);
	// 응용서비스 생성
	int insertAppService(@Param("obj") DbAppService obj);
	// 응용서비스 변경
	int updateAppService(@Param("obj") DbAppService obj);
	// 응용서비스 삭제
	int deleteAppService(@Param("service") String service);
}
