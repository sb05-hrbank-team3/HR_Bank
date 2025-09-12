-- 부서
CREATE TABLE departments
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100) NOT NULL,
    established_date DATE NOT NULL
);

-- 파일
CREATE TABLE binary_contents
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL
);

-- 직원 테이블 재정의
CREATE TABLE employees
(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(20) NOT NULL,
    employee_number VARCHAR(100) NOT NULL UNIQUE,
    hire_date DATE NOT NULL,
    position VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    binary_content_id BIGINT,
    department_id BIGINT NOT NULL,

    -- FK: BinaryContent 삭제 시 null
    CONSTRAINT fk_employee_file
        FOREIGN KEY (binary_content_id) REFERENCES binary_contents (id)
            ON DELETE SET NULL
            ON UPDATE CASCADE,

    -- FK: Department 삭제 시 Employee는 삭제되지 않음
    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id) REFERENCES departments (id)
            ON DELETE RESTRICT
            ON UPDATE CASCADE
);

-- 변경 로그
CREATE TABLE change_logs
(
    id          BIGSERIAL PRIMARY KEY,
    type        VARCHAR(20)  NOT NULL,
    ip_address  VARCHAR(100) NOT NULL,
    memo        VARCHAR(100) NOT NULL,
    at          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    employee_id BIGINT,  -- NULL 허용(직원 삭제 후에도 로그는 남김)
    employee_number VARCHAR(100) NOT NULL,

    CONSTRAINT fk_changelog_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id)
            ON DELETE SET NULL ON UPDATE CASCADE
);

-- 변경 이력 상세
CREATE TABLE histories
(
    id BIGSERIAL PRIMARY KEY,
    property_name VARCHAR(30),
    before VARCHAR(100),
    after VARCHAR(100),
    log_id BIGINT NOT NULL,
    CONSTRAINT fk_history_changelog
        FOREIGN KEY (log_id) REFERENCES change_logs (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

-- 백업
CREATE TABLE backups
(
    id BIGSERIAL PRIMARY KEY,
    worker VARCHAR(20) NOT NULL,
    started_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    ended_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL,
    binary_content_id BIGINT,
    CONSTRAINT fk_backup_file
        FOREIGN KEY (binary_content_id) REFERENCES binary_contents (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

-- -- -- 하위 테이블부터 삭제
-- DROP TABLE IF EXISTS histories CASCADE;
-- DROP TABLE IF EXISTS change_logs CASCADE;
-- DROP TABLE IF EXISTS backups CASCADE;
-- DROP TABLE IF EXISTS employees CASCADE;
-- DROP TABLE IF EXISTS binary_contents CASCADE;
-- DROP TABLE IF EXISTS departments CASCADE;
--
--
-- -- 전체 데이터 삭제
-- TRUNCATE TABLE histories CASCADE;
-- TRUNCATE TABLE change_logs CASCADE;
-- TRUNCATE TABLE backups CASCADE;
-- TRUNCATE TABLE employees CASCADE;
-- TRUNCATE TABLE binary_contents CASCADE;
-- TRUNCATE TABLE departments CASCADE;
--
-- CREATE USER hrbank_user WITH PASSWORD 'hrbank_1234';
-- GRANT ALL PRIVILEGES ON DATABASE hrbank TO hrbank_user;
-- -- ROLE이 모든 테이블/시퀀스 권한 가짐
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO hrbank_role;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO hrbank_role;
--
-- -- 앞으로 생성될 테이블/시퀀스도 자동 권한 부여
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO hrbank_role;
-- ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO hrbank_role;
--
-- GRANT ALL PRIVILEGES ON SCHEMA public TO hrbank_user;
--
-- GRANT hrbank_role TO hrbank_user;