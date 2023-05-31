package com.t3q.dranswer.config;

public class Constants {

	public static final String SLASH			= "/";
	public static final String COLONS			= ":";
	public static final String SPACE			= " ";

	// common code
	public static final String PREFIX_SVC		= "s";
	public static final String PREFIX_MIC		= "m";
	public static final String PREFIX_IMG		= "i";
	public static final String PREFIX_CON		= "c";

	// harbor url
	public static final String HARBOR_URL				= "svc-harbor-dev.com";
	// container set
	public static final String TYPE_OPEN				= "OPEN";
	public static final String TYPE_CONN				= "CONN";
	public static final String TYPE_MOUNT				= "MOUNT";
	public static final String TYPE_ENV					= "ENV";
	// cman url
	public static final String CMAN_PROJECT_CREATE_URL 					= "/api/v1/init/projects";
	public static final String CMAN_IMAGE_CREATE_URL 					= "/api/v1/images";
	public static final String CMAN_IMAGE_DELETE_URL 					= "/api/v1/images/{image_name}";
	public static final String CMAN_CONTAINER_CREATE_URL 				= "/api/v1/containers";
	public static final String CMAN_CONTAINER_LIST_READ_URL 			= "/api/v1/containers";
	public static final String CMAN_CONTAINER_UPDATE_URL 				= "/api/v1/containers/{conid}";
	public static final String CMAN_CONTAINER_DELETE_URL 				= "/api/v1/containers/{conid}";
	public static final String CMAN_CONTAINER_DEPLOY_URL 				= "/api/v1/containers/{conid}/deploy";
	public static final String CMAN_CONTAINER_RECYCLE_URL 				= "/api/v1/containers/{conid}/recycle";
	public static final String CMAN_CONTAINER_DOMAIN_CREATE_URL 		= "/api/v1/containers/{conid}/domain";
	public static final String CMAN_CONTAINER_DOMAIN_DELETE_URL 		= "/api/v1/containers/{conid}/domain";
	public static final String CMAN_CONTAINER_TIME_READ_URL 			= "/api/v1/containers/{conid}/times";
	public static final String CMAN_CONTAINER_POD_READ_URL 				= "/api/v1/containers/{conid}/pods";
	public static final String CMAN_CONTAINER_STATUS_READ_URL 			= "/api/v1/containers/{conid}/status/{podName}";
	public static final String CMAN_CONTAINER_LIST_STATUS_READ_URL 		= "/api/v1/containers/status/list";

	// container current status
	public static final String STATUS_RUN					= "RUN";
	public static final String STATUS_SUSPEND				= "SUSPEND";
	public static final String STATUS_POD_RUNNING			= "POD_RUNNING";
	public static final String STATUS_IMAGE_UPLOADING		= "IMAGE_UPLOADING";
	public static final String STATUS_POD_DEPLOYING			= "POD_DEPLOYING";
	public static final String STATUS_POD_TERMINATING		= "POD_TERMINATING";
	public static final String STATUS_POD_STOPPED			= "POD_STOPPED";
	public static final String STATUS_IMAGE_UPLOAD_FAILED	= "IMAGE_UPLOAD_FAILED";
	public static final String STATUS_DEPLOY_FAILED			= "DEPLOY_FAILED";
	// container status detail
	public static final String DETAIL_RUNNING				= "RUNNING";
	public static final String DETAIL_UPLOADING				= "UPLOADING";
	public static final String DETAIL_DEPLOYING				= "DEPLOYING";
	public static final String DETAIL_TERMINATING			= "TERMINATING";
	public static final String DETAIL_SHUTDOWN				= "SHUTDOWN";
	public static final String DETAIL_ERROR_LOAD			= "ERROR_LOAD";
	public static final String DETAIL_ERROR_PUSH			= "ERROR_PUSH";
	public static final String DETAIL_ERROR_IMAGE_PULL		= "ERROR_IMAGE_PULL";

	public static final String DETAIL_ERROR_OOM				= "ERROR_OOM";
	public static final String DETAIL_ERROR_PENDING			= "ERROR_PENDING";
	public static final String DETAIL_ERROR_CRASHED			= "ERROR_CRASHED";
	public static final String DETAIL_ERROR_INTERNAL_ERR	= "ERROR_INTERNAL_ERR";

	// error code
	public static final String E00000		 		= "00000";
	public static final String E00000_MSG	 		= "성공";
	public static final String E40001		 		= "40001";
	public static final String E40001_MSG	 		= "필수 항목이 없습니다.";
	public static final String E40002		 		= "40002";
	public static final String E40002_MSG	 		= "필수 헤더항목이 없습니다.";
	public static final String E40003		 		= "40003";
	public static final String E40003_MSG	 		= "토큰이 유효하지 않습니다.";
	public static final String E50001		 		= "50001";
	public static final String E50001_MSG	 		= "시스템에 접속할 수 없습니다.";
	public static final String E50002		 		= "50002";
	public static final String E50002_MSG	 		= "필요한 데이터가 없습니다.";
	public static final String E50000		 		= "50000";

}
