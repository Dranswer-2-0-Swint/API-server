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
    // 마이크로서비스도메인 조회
    public DbMicroDomain selectMicroDomain(@Param("microService") String microService);
    // 마이크로서비스도메인 목록 조회
    public List<DbMicroDomain> selectMicroDomainDomainByDomain(@Param("service") String domain);
    // 마이크로서비스도메인 생성
    public int insertMicroDomain(@Param("obj") DbMicroDomain obj);
    // 마이크로서비스도메인명 변경
    public int updateMicroDomainName(@Param("obj") DbMicroDomain obj);
    // 마이크로서비스도메인 변경
    public int updateMicroDomain(@Param("obj") DbMicroDomain obj);
    // 마이크로서비스 삭제
    public int deleteMicroDomain(@Param("MicroDomain") String MicroDomain);
    // 응용서비스에 속한 마이크로서비스 삭제
    public int deleteMicroDomainByService(@Param("service") String service);

    // 컨테이너 조회(마이크로서비스 도메인 변경 시 해당하는 모든 컨테이너의 외부 도메인을 변경)
    public List<String> selectContainerByMicro(@Param("micro") String micro);
    // 응용서비스 조회
    public String selectServiceByMicro(@Param("micro") String micro);
    // 컨테이너 삭제
    public int deleteContainer(@Param("container") String container);
    // 이미지 목록 조회
    public List<String> selectImageByMicro(@Param("micro") String micro);
}
