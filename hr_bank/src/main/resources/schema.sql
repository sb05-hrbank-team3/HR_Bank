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


--batch용
-- ===============================
-- Create Spring Batch tables (PostgreSQL, Spring Batch 5.x)
-- ===============================
-- ===============================
-- Create sequences
-- ===============================
CREATE SEQUENCE BATCH_JOB_INSTANCE_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ START WITH 1 INCREMENT BY 1;

-- ===============================
-- 2. Create tables
-- ===============================


-- Job Instance
CREATE TABLE BATCH_JOB_INSTANCE (
                                    JOB_INSTANCE_ID BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('BATCH_JOB_INSTANCE_SEQ'),
                                    VERSION BIGINT,
                                    JOB_NAME VARCHAR(100) NOT NULL,
                                    JOB_KEY VARCHAR(32) NOT NULL,
                                    UNIQUE (JOB_NAME, JOB_KEY)
);

-- Job Execution
CREATE TABLE BATCH_JOB_EXECUTION (
                                     JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('BATCH_JOB_EXECUTION_SEQ'),
                                     VERSION BIGINT NOT NULL,
                                     JOB_INSTANCE_ID BIGINT NOT NULL,
                                     CREATE_TIME TIMESTAMP NOT NULL,
                                     START_TIME TIMESTAMP,
                                     END_TIME TIMESTAMP,
                                     STATUS VARCHAR(10),
                                     EXIT_CODE VARCHAR(20),
                                     EXIT_MESSAGE VARCHAR(2500),
                                     LAST_UPDATED TIMESTAMP,
                                     JOB_CONFIGURATION_LOCATION VARCHAR(2500),
                                     CONSTRAINT JOB_INST_EXEC_FK FOREIGN KEY(JOB_INSTANCE_ID)
                                         REFERENCES BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
);

-- Job Execution Parameters (Spring Batch 5.x)
CREATE TABLE BATCH_JOB_EXECUTION_PARAMS (
                                            JOB_EXECUTION_ID BIGINT NOT NULL,
                                            PARAMETER_NAME VARCHAR(100) NOT NULL,
                                            PARAMETER_TYPE VARCHAR(1000) NOT NULL,
                                            PARAMETER_VALUE VARCHAR(2500),
                                            IDENTIFYING CHAR(1) NOT NULL,
                                            CONSTRAINT JOB_EXEC_PARAMS_FK FOREIGN KEY(JOB_EXECUTION_ID)
                                                REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- Step Execution
CREATE TABLE BATCH_STEP_EXECUTION (
                                      STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY DEFAULT nextval('BATCH_STEP_EXECUTION_SEQ'),
                                      VERSION BIGINT NOT NULL,
                                      STEP_NAME VARCHAR(100) NOT NULL,
                                      JOB_EXECUTION_ID BIGINT NOT NULL,
                                      START_TIME TIMESTAMP NOT NULL,
                                      END_TIME TIMESTAMP,
                                      STATUS VARCHAR(10),
                                      COMMIT_COUNT BIGINT,
                                      READ_COUNT BIGINT,
                                      FILTER_COUNT BIGINT,
                                      WRITE_COUNT BIGINT,
                                      EXIT_CODE VARCHAR(20),
                                      EXIT_MESSAGE VARCHAR(2500),
                                      LAST_UPDATED TIMESTAMP,
                                      CONSTRAINT STEP_EXEC_JOB_FK FOREIGN KEY(JOB_EXECUTION_ID)
                                          REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

-- Step Execution Context
CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT (
                                              STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                              SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                              SERIALIZED_CONTEXT TEXT,
                                              CONSTRAINT STEP_EXEC_CTX_FK FOREIGN KEY(STEP_EXECUTION_ID)
                                                  REFERENCES BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
);

-- Job Execution Context
CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT (
                                             JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                             SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                             SERIALIZED_CONTEXT TEXT,
                                             CONSTRAINT JOB_EXEC_CTX_FK FOREIGN KEY(JOB_EXECUTION_ID)
                                                 REFERENCES BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
);

CREATE INDEX idx_employee_name_id ON employees(name ASC, id ASC);

