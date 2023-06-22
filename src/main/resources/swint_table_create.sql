-- swint.swint_svc_seq definition

-- DROP SEQUENCE swint.swint_svc_seq;

CREATE SEQUENCE swint.swint_svc_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999999
    START 1
	CACHE 1
	NO CYCLE;




-- swint.swint_mic_seq definition

-- DROP SEQUENCE swint.swint_mic_seq;

CREATE SEQUENCE swint.swint_mic_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999999
    START 1
	CACHE 1
	NO CYCLE;




-- swint.swint_img_seq definition

-- DROP SEQUENCE swint.swint_img_seq;

CREATE SEQUENCE swint.swint_img_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999999
    START 1
	CACHE 1
	NO CYCLE;




-- swint.swint_con_seq definition

-- DROP SEQUENCE swint.swint_con_seq;

CREATE SEQUENCE swint.swint_con_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999999
    START 1
	CACHE 1
	NO CYCLE;


-- swint.swint-api_seq definition

-- DROP SEQUENCE swint.swint_api_seq;

CREATE SEQUENCE swint.swint_api_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 99999999999
    START 1
  CACHE 1
  NO CYCLE;



-- swint.tb_service_info definition

-- Drop table

-- DROP TABLE swint.tb_service_info;

CREATE TABLE swint.tb_service_info (
                                       service varchar NOT NULL, -- 응용서비스ID
                                       company varchar NOT NULL, -- 기업ID
                                       service_name varchar NULL, -- 응용서비스명
                                       mod_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 변경일시
                                       reg_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 등록일시
                                       CONSTRAINT tb_service_info_pk PRIMARY KEY (service)
);
COMMENT ON TABLE swint.tb_service_info IS '기업에 생성된  응용서비스 저장';

-- Column comments

COMMENT ON COLUMN swint.tb_service_info.service IS '응용서비스ID';
COMMENT ON COLUMN swint.tb_service_info.company IS '기업ID';
COMMENT ON COLUMN swint.tb_service_info.service_name IS '응용서비스명';
COMMENT ON COLUMN swint.tb_service_info.mod_timestamp IS '변경일시';
COMMENT ON COLUMN swint.tb_service_info.reg_timestamp IS '등록일시';





-- swint.tb_micro_info definition

-- Drop table

-- DROP TABLE swint.tb_micro_info;

CREATE TABLE swint.tb_micro_info (
                                     micro_service varchar NOT NULL, -- 마이크로서비스ID
                                     service varchar NOT NULL, -- 응용서비스ID
                                     micro_service_name varchar NOT NULL, -- 마이크로서비스명
                                     mod_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 변경일시
                                     reg_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 등록일시
                                     CONSTRAINT tb_micro_service_pk PRIMARY KEY (micro_service)
);
COMMENT ON TABLE swint.tb_micro_info IS '기업 응용서비스에 생성된 마이크로서비스 저장';

-- Column comments

COMMENT ON COLUMN swint.tb_micro_info.micro_service IS '마이크로서비스ID';
COMMENT ON COLUMN swint.tb_micro_info.service IS '응용서비스ID';
COMMENT ON COLUMN swint.tb_micro_info.micro_service_name IS '마이크로서비스명';
COMMENT ON COLUMN swint.tb_micro_info.mod_timestamp IS '변경일시';
COMMENT ON COLUMN swint.tb_micro_info.reg_timestamp IS '등록일시';


-- swint.tb_micro_info foreign keys

ALTER TABLE swint.tb_micro_info ADD CONSTRAINT tb_micro_service_fk FOREIGN KEY (service) REFERENCES swint.tb_service_info(service) ON DELETE CASCADE ON UPDATE CASCADE;








-- swint.tb_micro_domain definition

-- Drop table

-- DROP TABLE swint.tb_micro_domain;

CREATE TABLE swint.tb_micro_domain (
                                     micro_service varchar NOT NULL, -- 마이크로서비스ID
                                     domain varchar NOT NULL, -- 마이크로서비스도메인
                                     path varchar NOT NULL, -- 마이크로서비스도메인패스
                                     port varchar NOT NULL, -- 마이크로서비스포트
                                     mod_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 변경일시
                                     reg_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 등록일시
                                     CONSTRAINT tb_micro_domain_pk PRIMARY KEY (micro_service, domain, path)
);
COMMENT ON TABLE swint.tb_micro_domain IS '기업 응용서비스에 생성된 마이크로서비스 도메인 저장';

-- Column comments

COMMENT ON COLUMN swint.tb_micro_domain.micro_service IS '마이크로서비스ID';
COMMENT ON COLUMN swint.tb_micro_domain.domain IS '마이크로서비스도메인';
COMMENT ON COLUMN swint.tb_micro_domain.path IS '마이크로서비스도메인패스';
COMMENT ON COLUMN swint.tb_micro_domain.port IS '마이크로서비스포트';
COMMENT ON COLUMN swint.tb_micro_domain.mod_timestamp IS '변경일시';
COMMENT ON COLUMN swint.tb_micro_domain.reg_timestamp IS '등록일시';


-- swint.tb_micro_domain foreign keys

ALTER TABLE swint.tb_micro_domain ADD CONSTRAINT tb_micro_domain_fk FOREIGN KEY (micro_service) REFERENCES swint.tb_micro_info(micro_service) ON DELETE CASCADE ON UPDATE CASCADE;








-- swint.tb_image_info definition

-- Drop table

-- DROP TABLE swint.tb_image_info;

CREATE TABLE swint.tb_image_info (
                                     image varchar NOT NULL, -- 이미지ID
                                     micro_service varchar NOT NULL, -- 마이크로서비스ID
                                     image_name varchar NOT NULL, -- 이미지명
                                     image_real_name varchar NULL, -- 이미지명(하버)
                                     image_status varchar NOT NULL, -- 이미지상태
                                     image_status_detail varchar NOT NULL, -- 이미지상태상세
                                     mod_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 변경일시
                                     reg_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 등록일시
                                     CONSTRAINT tb_image_info_pk PRIMARY KEY (image)
);
COMMENT ON TABLE swint.tb_image_info IS '기업의 이미지 정보를 저장';

-- Column comments

COMMENT ON COLUMN swint.tb_image_info.image IS '이미지ID';
COMMENT ON COLUMN swint.tb_image_info.micro_service IS '마이크로서비스ID';
COMMENT ON COLUMN swint.tb_image_info.image_name IS '이미지명';
COMMENT ON COLUMN swint.tb_image_info.image_real_name IS '이미지명(하버)';
COMMENT ON COLUMN swint.tb_image_info.image_status IS '이미지상태';
COMMENT ON COLUMN swint.tb_image_info.image_status_detail IS '이미지상태상세';
COMMENT ON COLUMN swint.tb_image_info.mod_timestamp IS '변경일시';
COMMENT ON COLUMN swint.tb_image_info.reg_timestamp IS '등록일시';


-- swint.tb_image_info foreign keys

ALTER TABLE swint.tb_image_info ADD CONSTRAINT tb_image_info_fk FOREIGN KEY (micro_service) REFERENCES swint.tb_micro_info(micro_service) ON DELETE CASCADE ON UPDATE CASCADE;








-- swint.tb_container_info definition

-- Drop table

-- DROP TABLE swint.tb_container_info;

CREATE TABLE swint.tb_container_info (
                                         image varchar NOT NULL, -- 이미지ID
                                         container varchar NOT NULL, -- 컨테이너ID
                                         domain varchar NOT NULL, -- 이미지도메인
                                         port varchar NOT NULL, -- 이미지포트
                                         mod_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 변경일시
                                         reg_timestamp timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 등록일시
                                         CONSTRAINT tb_container_info_pk PRIMARY KEY (container, domain)
);
COMMENT ON TABLE swint.tb_container_info IS '기업의 컨테이너 정보 저장';

-- Column comments

COMMENT ON COLUMN swint.tb_container_info.image IS '이미지ID';
COMMENT ON COLUMN swint.tb_container_info.container IS '컨테이너ID';
COMMENT ON COLUMN swint.tb_container_info.domain IS '이미지도메인';
COMMENT ON COLUMN swint.tb_container_info.port IS '이미지포트';
COMMENT ON COLUMN swint.tb_container_info.mod_timestamp IS '변경일시';
COMMENT ON COLUMN swint.tb_container_info.reg_timestamp IS '등록일시';


-- swint.tb_container_info foreign keys

ALTER TABLE swint.tb_container_info ADD CONSTRAINT tb_container_info_fk FOREIGN KEY (image) REFERENCES swint.tb_image_info(image) ON DELETE CASCADE ON UPDATE CASCADE;

-- swint.tb_api_log definition

-- Drop table

-- DROP TABLE swint.tb_api_log;

CREATE TABLE swint.tb_api_log (
                                  seq_id bigint NOT NULL,
                                  req_id varchar NOT NULL, -- 요청ID
                                  req_user varchar NOT NULL, -- API 요청자
                                  req_md varchar NOT NULL, -- API Method
                                  req_uri varchar NOT NULL, -- API URI
                                  req_prm varchar NOT NULL, -- 요청 param
                                  req_body varchar NOT NULL, -- 요청 Body
                                  req_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 요청 일시
                                  res_user varchar NOT NULL, -- API 응답자
                                  res_msg varchar NOT NULL, -- 응답 메시지
                                  res_status varchar NOT NULL, -- 응답 상태코드
                                  res_body varchar NOT NULL, -- 응답 Body
                                  res_dt timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 응답 일시
                                  CONSTRAINT tb_api_log_pk PRIMARY KEY (seq_id)
);
COMMENT ON TABLE swint.tb_api_log IS 'servpot 과 cman의 request reponse 로깅';

-- Column comments

COMMENT ON COLUMN swint.tb_api_log.seq_id IS '로그ID';
COMMENT ON COLUMN swint.tb_api_log.req_id IS '요청ID';
COMMENT ON COLUMN swint.tb_api_log.req_user IS 'API 요청자';
COMMENT ON COLUMN swint.tb_api_log.req_md IS 'API Method';
COMMENT ON COLUMN swint.tb_api_log.req_uri IS 'API URI';
COMMENT ON COLUMN swint.tb_api_log.req_prm IS '요청 param';
COMMENT ON COLUMN swint.tb_api_log.req_body IS '요청 Body';
COMMENT ON COLUMN swint.tb_api_log.req_dt IS '요청 일시';
COMMENT ON COLUMN swint.tb_api_log.res_user IS 'API 응답자';
COMMENT ON COLUMN swint.tb_api_log.res_msg IS '응답 메시지';
COMMENT ON COLUMN swint.tb_api_log.res_status IS '응답 상태코드';
COMMENT ON COLUMN swint.tb_api_log.res_body IS '응답 Body';
COMMENT ON COLUMN swint.tb_api_log.res_dt IS '응답 일시';
