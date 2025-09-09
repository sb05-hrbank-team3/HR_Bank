-- 부서
CREATE TABLE departments
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(100) NOT NULL,
    established_date TIMESTAMP WITH TIME ZONE NOT NULL
);

-- 파일
CREATE TABLE binary_contents
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL
);

-- 직원
CREATE TABLE employees
(
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(20) NOT NULL,
    employee_number VARCHAR(100) NOT NULL UNIQUE,
    hire_date TIMESTAMP WITH TIME ZONE NOT NULL,
    position VARCHAR(30) NOT NULL,
    status VARCHAR(20) NOT NULL,
    binary_content_id BIGINT,
    department_id BIGINT NOT NULL,
    CONSTRAINT fk_employee_file
        FOREIGN KEY (binary_content_id) REFERENCES binary_contents (id)
            ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id) REFERENCES departments (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);

-- 변경 로그
CREATE TABLE change_logs
(
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(20) NOT NULL,
    ip_address VARCHAR(100) NOT NULL,
    memo VARCHAR(100) NOT NULL,
    at TIMESTAMP WITH TIME ZONE NOT NULL,
    employee_id BIGINT NOT NULL,
    CONSTRAINT fk_changelog_employee
        FOREIGN KEY (employee_id) REFERENCES employees (id)
            ON DELETE CASCADE ON UPDATE CASCADE
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
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    ended_at TIMESTAMP WITH TIME ZONE NOT NULL,
    status VARCHAR(20) NOT NULL,
    binary_content_id BIGINT NOT NULL,
    CONSTRAINT fk_backup_file
        FOREIGN KEY (binary_content_id) REFERENCES binary_contents (id)
            ON DELETE CASCADE ON UPDATE CASCADE
);







