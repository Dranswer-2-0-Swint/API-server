package com.t3q.dranswer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.t3q.dranswer.dto.db.DbImage;

@Mapper
@Repository
public interface ImageMapper {
	// 기업의 이미지 순번
	public int getImageSequence();
	// 기업의 컨테이너 순번
	public int getContainerSequence();
	// 응용서비스ID 조회
	public String selectServiceByMicro(@Param("micro") String micro);
	// 이미지 조회
	public DbImage selectImageByimage(@Param("image") String image);
	// 마이크로서비스ID로 이미지 목록 조회
	public List<DbImage> selectImageByMicro(@Param("microService") String microService);
	// 마이크로서비스ID로 컨테이너 목록 조회
	//public List<DbImage> selectContainerByMicro(@Param("microService") String microService);
	// 이미지 등록
	int insertImage(@Param("obj") DbImage obj);
	// 이미지 이름 변경(harbor)
	int updateImageRealName(@Param("image") String image, @Param("imageRealName") String imageRealName);
	// 이미지 상태 변경
	int updateImageStatus(@Param("image") String image, @Param("imageStatus") String imageStatus, @Param("imageStatusDetail") String imageStatusDetail);
	// 컨테이너ID, 컨테이너 도메인 변경
	int updateImageContainer(@Param("image") String image, @Param("container") String container, @Param("containerDomain") String containerDomain);
	// 이미지 삭제
	int deleteImageByImage(@Param("image") String image);
	// 마이크로서비스에 속한 이미지 삭제
	int deleteImageByMicro(@Param("microService") String microService);
}
