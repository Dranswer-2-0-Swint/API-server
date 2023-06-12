package com.t3q.dranswer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.t3q.dranswer.dto.db.DbContainer;
import com.t3q.dranswer.dto.db.DbImage;

@Mapper
@Repository
public interface ImageMapper {
	// 기업의 이미지 순번
	public int getImageSequence();
	// 응용서비스ID 조회
	public String selectServiceByMicro(@Param("micro") String micro);
	// 응용서비스ID 조회
	public String selectServiceByImage(@Param("image") String image);
	// 이미지 조회
	public DbImage selectImage(@Param("image") String image);
	// 마이크로서비스ID로 이미지 목록 조회
	public List<DbImage> selectImageByMicro(@Param("microService") String microService);
	// 마이크로서비스 도메인 조회
	public String selectDomainByMicro(@Param("microService") String microService);
	// 이미지 등록
	public int insertImage(@Param("obj") DbImage obj);
	// 이미지 이름 변경(harbor)
	public int updateImageRealName(@Param("image") String image, @Param("imageRealName") String imageRealName);
	// 이미지 상태 변경
	public int updateImageStatus(@Param("image") String image, @Param("imageStatus") String imageStatus, @Param("imageStatusDetail") String imageStatusDetail);
	// 이미지 삭제
	public int deleteImage(@Param("image") String image);
	// 마이크로서비스에 속한 이미지 삭제
	public int deleteImageByMicro(@Param("microService") String microService);
	
	
	// 기업의 컨테이너 순번
	public int getContainerSequence();
	// 이미지ID 조회
	public String selectImageByContainer(@Param("container") String container);
	// 컨테이너 목록 조회
	public List<DbContainer> selectContainerByImage(@Param("image") String image);
	// 컨테이너 조회
	public String selectContainerIdByImage(@Param("image") String image);
	// 컨테이너 목록 등록
	public int insertContainer(@Param("list") List<DbContainer> list);
	// 컨테이너 삭제
	public int deleteContainerByImage(@Param("image") String image);

}
