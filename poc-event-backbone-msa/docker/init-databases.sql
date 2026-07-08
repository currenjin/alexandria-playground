-- MSA: 서비스마다 자기 DB (§7.1.3 DB-per-service). 예제는 한 PG 인스턴스에 4 DB.
-- orchestrator = 중앙 사가(플랫폼 오너 소유)의 saga_instance DB (§7.1.7).
CREATE DATABASE orchestrator;
CREATE DATABASE oms;
CREATE DATABASE tms;
CREATE DATABASE bms;
