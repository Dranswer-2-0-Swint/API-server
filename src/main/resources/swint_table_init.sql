
-- 현재는 기업의 응용서비스 샘플 데이터를 미리 등록하므로 ID 충돌하지 않도록 여유있게 100으로 설정
SELECT setval('swint_svc_seq', 100, true);

-- 응용서비스 샘플 데이터
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s6dd28e9b', 'life-1', 'life-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('sf3b61b38', 'jlk', 'jlk-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s84b12bae', 'nanaps', 'nanaps-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s1db87a14', 'coreit', 'coreit-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s6abf4a82', 'life-2', 'life-svc-2');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s8d076785', 'moco', 'moco-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('sa15d25e1', 'visual', 'visual-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s3854745b', 'planit', 'planit-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('sa630e1f8', 'acryl', 'acryl-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('sd137d16e', 'pct', 'pct-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s483e80d4', 'aidot', 'aidot-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('s3f39b042', 'deepai', 'deepai-svc-1');
INSERT INTO swint.tb_service_info
(service, company, service_name)
VALUES('saf86add3', 'eztech', 'eztech-svc-1');
