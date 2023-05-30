package com.t3q.dranswer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.t3q.dranswer.dto.db.DbMicroService;

@Mapper
@Repository
public interface MicroServiceMapper {
	// 기업의 마이크로서비스 순번
	public int getMicroSequence();
	// 마이크로서비스 조회
	public DbMicroService selectMicroServiceByMicro(@Param("microService") String microService);
	// 마이크로서비스 목록 조회
	public List<DbMicroService> selectMicroServiceByService(@Param("service") String service);
	// 마이크로서비스 생성
	int insertMicroService(@Param("obj") DbMicroService obj);
	// 마이크로서비스명 변경
	int updateMicroServiceName(@Param("obj") DbMicroService obj);
	// 마이크로서비스 도메인 변경
	int updateMicroServiceDomain(@Param("obj") DbMicroService obj);
	// 마이크로서비스 삭제
	int deleteMicroServiceByMicro(@Param("microService") String microService);
	// 응용서비스에 속한 마이크로서비스 삭제
	int deleteMicroServiceByService(@Param("service") String service);

	// 컨테이너 조회(마이크로서비스 도메인 변경 시 해당하는 모든 컨테이너의 외부 도메인을 변경)
	public List<String> selectContainerForDomain(@Param("micro") String micro);
}
