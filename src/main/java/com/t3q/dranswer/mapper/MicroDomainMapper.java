package com.t3q.dranswer.mapper;

import com.t3q.dranswer.dto.db.DbMicroDomain;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainDeleteReq;
import com.t3q.dranswer.dto.servpot.ServpotMicroServiceDomainUpdateReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface MicroDomainMapper {

    // 마이크로서비스도메인 조회 V
    public DbMicroDomain selectMicroDomain(@Param("domain") String domain);
    // 마이크로서비스도메인 목록 조회 V
    public List<DbMicroDomain> selectMicroDomainByMicro(@Param("microService") String microService);
    // 마이크로서비스도메인 생성 V
    public int insertMicroDomain(@Param("obj") DbMicroDomain obj);
    // 마이크로서비스도메인 변경 V
    public int updateMicroDomain(@Param("obj") ServpotMicroServiceDomainUpdateReq obj);
    // 마이크로서비스도메인 삭제 V
    public int deleteMicroDomainByMicro(@Param("microService") String microService);
    // 마이크로서비스도메인 삭제 V
    public int deleteMicroDomain(@Param("obj") ServpotMicroServiceDomainDeleteReq obj);
}
