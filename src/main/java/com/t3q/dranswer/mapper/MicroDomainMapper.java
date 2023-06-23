package com.t3q.dranswer.mapper;

import com.t3q.dranswer.dto.db.DbMicroDomain;
import com.t3q.dranswer.dto.db.DbMicroDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface MicroDomainMapper {

    public int getMicroSequence();
    // 마이크로서비스도메인 조회 V
    public DbMicroDomain selectMicroDomain(@Param("microService") String microService);
    // 마이크로서비스도메인 목록 조회 V
    public List<DbMicroDomain> selectMicroDomainByService(@Param("microService") String microService);
    // 마이크로서비스도메인 생성 V
    public int insertMicroDomain(@Param("obj") DbMicroDomain obj);
    // 마이크로서비스도메인 변경 V
    public int updateMicroDomain(@Param("obj") DbMicroDomain obj);
    // 마이크로서비스도메인 삭제 V
    public int deleteMicroDomain(@Param("microService") String microService);
}
