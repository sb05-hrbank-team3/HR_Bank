-- 부서
CREATE TABLE departments
(
    id               BIGINT PRIMARY KEY,
    name             VARCHAR(20)  NOT NULL,
    description      VARCHAR(100) NOT NULL,
    established_date DATE         NOT NULL
);

-- 파일
CREATE TABLE binary_contents
(
    id           BIGINT PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL
);

-- 직원
CREATE TABLE employees
(
    id                BIGINT PRIMARY KEY,
    email             VARCHAR(100) NOT NULL UNIQUE,
    name              VARCHAR(20)  NOT NULL,
    employee_number   VARCHAR(100) NOT NULL UNIQUE,
    hire_date         DATE         NOT NULL,
    position          VARCHAR(30)  NOT NULL,
    status            VARCHAR(20)  NOT NULL,
    binary_content_id BIGINT,
    department_id     BIGINT       NOT NULL,
    CONSTRAINT fk_employee_file FOREIGN KEY (file_id) REFERENCES files (id),
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments (id)
);

-- 변경 로그
CREATE TABLE change_logs
(
    id          BIGINT PRIMARY KEY,
    type        VARCHAR(20)  NOT NULL, -- Enum 가정
    ip_address  VARCHAR(100) NOT NULL,
    memo        VARCHAR(100) NOT NULL,
    at          DATE         NOT NULL,
    employee_id BIGINT       NOT NULL,
    CONSTRAINT fk_changelog_employee FOREIGN KEY (employee_id) REFERENCES employees (id)
);

-- 변경 이력 상세
CREATE TABLE histories
(
    id            BIGINT PRIMARY KEY,
    property_name VARCHAR(30),
    before        VARCHAR(100),
    after         VARCHAR(100),
    log_id        BIGINT NOT NULL,
    CONSTRAINT fk_history_changelog FOREIGN KEY (log_id) REFERENCES change_logs (id)
);

-- 백업
CREATE TABLE backups
(
    id                BIGINT PRIMARY KEY,
    worker            VARCHAR(20) NOT NULL,
    started_at        DATE        NOT NULL,
    ended_at          DATE        NOT NULL,
    status            VARCHAR(20) NOT NULL, -- Enum 가정
    binary_content_id BIGINT      NOT NULL,
    CONSTRAINT fk_backup_file FOREIGN KEY (file_id) REFERENCES files (id)
);