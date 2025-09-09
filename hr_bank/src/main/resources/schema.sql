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

-- 1. 부서 (20개)
INSERT INTO departments (id, name, description, established_date)
VALUES (1, 'Dept1', '테스트 부서 1', '2010-01-01'),
       (2, 'Dept2', '테스트 부서 2', '2010-02-01'),
       (3, 'Dept3', '테스트 부서 3', '2010-03-01'),
       (4, 'Dept4', '테스트 부서 4', '2010-04-01'),
       (5, 'Dept5', '테스트 부서 5', '2010-05-01'),
       (6, 'Dept6', '테스트 부서 6', '2010-06-01'),
       (7, 'Dept7', '테스트 부서 7', '2010-07-01'),
       (8, 'Dept8', '테스트 부서 8', '2010-08-01'),
       (9, 'Dept9', '테스트 부서 9', '2010-09-01'),
       (10, 'Dept10', '테스트 부서 10', '2010-10-01'),
       (11, 'Dept11', '테스트 부서 11', '2011-01-01'),
       (12, 'Dept12', '테스트 부서 12', '2011-02-01'),
       (13, 'Dept13', '테스트 부서 13', '2011-03-01'),
       (14, 'Dept14', '테스트 부서 14', '2011-04-01'),
       (15, 'Dept15', '테스트 부서 15', '2011-05-01'),
       (16, 'Dept16', '테스트 부서 16', '2011-06-01'),
       (17, 'Dept17', '테스트 부서 17', '2011-07-01'),
       (18, 'Dept18', '테스트 부서 18', '2011-08-01'),
       (19, 'Dept19', '테스트 부서 19', '2011-09-01'),
       (20, 'Dept20', '테스트 부서 20', '2011-10-01');

-- 2. 파일 (20개)
INSERT INTO binary_contents (id, name, size, content_type)
VALUES (1, 'file1.png', 1024, 'image/png'),
       (2, 'file2.jpg', 2048, 'image/jpeg'),
       (3, 'file3.zip', 4096, 'application/zip'),
       (4, 'file4.pdf', 8192, 'application/pdf'),
       (5, 'file5.docx', 5120,
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document'),
       (6, 'file6.xlsx', 6144, 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'),
       (7, 'file7.pptx', 7168,
        'application/vnd.openxmlformats-officedocument.presentationml.presentation'),
       (8, 'file8.txt', 1024, 'text/plain'),
       (9, 'file9.csv', 2048, 'text/csv'),
       (10, 'file10.png', 3072, 'image/png'),
       (11, 'file11.jpg', 4096, 'image/jpeg'),
       (12, 'file12.zip', 8192, 'application/zip'),
       (13, 'file13.pdf', 12288, 'application/pdf'),
       (14, 'file14.docx', 14336,
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document'),
       (15, 'file15.xlsx', 16384,
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'),
       (16, 'file16.pptx', 18432,
        'application/vnd.openxmlformats-officedocument.presentationml.presentation'),
       (17, 'file17.txt', 2048, 'text/plain'),
       (18, 'file18.csv', 3072, 'text/csv'),
       (19, 'file19.png', 4096, 'image/png'),
       (20, 'file20.jpg', 5120, 'image/jpeg');

-- 3. 직원 (20개) → 부서/파일과 FK 맞춤
INSERT INTO employees (id, email, name, employee_number, hire_date, position, status,
                       binary_content_id, department_id)
VALUES (1, 'user1@example.com', 'User1', 'EMP001', '2020-01-01', 'Staff', 'ACTIVE', 1, 1),
       (2, 'user2@example.com', 'User2', 'EMP002', '2020-02-01', 'Manager', 'ACTIVE', 2, 2),
       (3, 'user3@example.com', 'User3', 'EMP003', '2020-03-01', 'Developer', 'ACTIVE', 3, 3),
       (4, 'user4@example.com', 'User4', 'EMP004', '2020-04-01', 'Staff', 'ACTIVE', 4, 4),
       (5, 'user5@example.com', 'User5', 'EMP005', '2020-05-01', 'Manager', 'INACTIVE', 5, 5),
       (6, 'user6@example.com', 'User6', 'EMP006', '2020-06-01', 'Developer', 'ACTIVE', 6, 6),
       (7, 'user7@example.com', 'User7', 'EMP007', '2020-07-01', 'Staff', 'ACTIVE', 7, 7),
       (8, 'user8@example.com', 'User8', 'EMP008', '2020-08-01', 'Manager', 'ACTIVE', 8, 8),
       (9, 'user9@example.com', 'User9', 'EMP009', '2020-09-01', 'Developer', 'ACTIVE', 9, 9),
       (10, 'user10@example.com', 'User10', 'EMP010', '2020-10-01', 'Staff', 'INACTIVE', 10, 10),
       (11, 'user11@example.com', 'User11', 'EMP011', '2020-11-01', 'Manager', 'ACTIVE', 11, 11),
       (12, 'user12@example.com', 'User12', 'EMP012', '2020-12-01', 'Developer', 'ACTIVE', 12, 12),
       (13, 'user13@example.com', 'User13', 'EMP013', '2021-01-01', 'Staff', 'ACTIVE', 13, 13),
       (14, 'user14@example.com', 'User14', 'EMP014', '2021-02-01', 'Manager', 'ACTIVE', 14, 14),
       (15, 'user15@example.com', 'User15', 'EMP015', '2021-03-01', 'Developer', 'INACTIVE', 15,
        15),
       (16, 'user16@example.com', 'User16', 'EMP016', '2021-04-01', 'Staff', 'ACTIVE', 16, 16),
       (17, 'user17@example.com', 'User17', 'EMP017', '2021-05-01', 'Manager', 'ACTIVE', 17, 17),
       (18, 'user18@example.com', 'User18', 'EMP018', '2021-06-01', 'Developer', 'ACTIVE', 18, 18),
       (19, 'user19@example.com', 'User19', 'EMP019', '2021-07-01', 'Staff', 'ACTIVE', 19, 19),
       (20, 'user20@example.com', 'User20', 'EMP020', '2021-08-01', 'Manager', 'ACTIVE', 20, 20);

-- 4. 변경 로그 (20개) → 직원 FK 맞춤
INSERT INTO change_logs (id, type, ip_address, memo, at, employee_id)
VALUES (1, 'UPDATE', '192.168.0.1', '직급 변경', '2025-01-01', 1),
       (2, 'DELETE', '192.168.0.2', '퇴사 처리', '2025-01-02', 2),
       (3, 'INSERT', '192.168.0.3', '신규 등록', '2025-01-03', 3),
       (4, 'UPDATE', '192.168.0.4', '부서 이동', '2025-01-04', 4),
       (5, 'UPDATE', '192.168.0.5', '상태 변경', '2025-01-05', 5),
       (6, 'DELETE', '192.168.0.6', '삭제 처리', '2025-01-06', 6),
       (7, 'INSERT', '192.168.0.7', '신규 등록', '2025-01-07', 7),
       (8, 'UPDATE', '192.168.0.8', '직급 변경', '2025-01-08', 8),
       (9, 'DELETE', '192.168.0.9', '퇴사 처리', '2025-01-09', 9),
       (10, 'INSERT', '192.168.0.10', '신규 등록', '2025-01-10', 10),
       (11, 'UPDATE', '192.168.0.11', '부서 이동', '2025-01-11', 11),
       (12, 'DELETE', '192.168.0.12', '퇴사 처리', '2025-01-12', 12),
       (13, 'INSERT', '192.168.0.13', '신규 등록', '2025-01-13', 13),
       (14, 'UPDATE', '192.168.0.14', '직급 변경', '2025-01-14', 14),
       (15, 'DELETE', '192.168.0.15', '퇴사 처리', '2025-01-15', 15),
       (16, 'INSERT', '192.168.0.16', '신규 등록', '2025-01-16', 16),
       (17, 'UPDATE', '192.168.0.17', '부서 이동', '2025-01-17', 17),
       (18, 'DELETE', '192.168.0.18', '퇴사 처리', '2025-01-18', 18),
       (19, 'INSERT', '192.168.0.19', '신규 등록', '2025-01-19', 19),
       (20, 'UPDATE', '192.168.0.20', '직급 변경', '2025-01-20', 20);

-- 5. 변경 이력 상세 (20개) → 로그 FK 맞춤
INSERT INTO histories (id, property_name, before, after, log_id)
VALUES (1, 'position', 'Staff', 'Manager', 1),
       (2, 'status', 'ACTIVE', 'INACTIVE', 2),
       (3, 'department', 'IT', 'HR', 3),
       (4, 'position', 'Developer', 'Manager', 4),
       (5, 'status', 'INACTIVE', 'ACTIVE', 5),
       (6, 'department', 'Finance', 'IT', 6),
       (7, 'position', 'Staff', 'Developer', 7),
       (8, 'status', 'ACTIVE', 'INACTIVE', 8),
       (9, 'department', 'HR', 'Finance', 9),
       (10, 'position', 'Manager', 'Staff', 10),
       (11, 'status', 'INACTIVE', 'ACTIVE', 11),
       (12, 'department', 'IT', 'Finance', 12),
       (13, 'position', 'Developer', 'Manager', 13),
       (14, 'status', 'ACTIVE', 'INACTIVE', 14),
       (15, 'department', 'Finance', 'HR', 15),
       (16, 'position', 'Staff', 'Manager', 16),
       (17, 'status', 'INACTIVE', 'ACTIVE', 17),
       (18, 'department', 'HR', 'IT', 18),
       (19, 'position', 'Manager', 'Developer', 19),
       (20, 'status', 'ACTIVE', 'INACTIVE', 20);

INSERT INTO backups (id, worker, started_at, ended_at, status, binary_content_id)
VALUES (1, 'admin', '2025-01-01', '2025-01-01', 'COMPLETED', 3),
       (2, 'system', '2025-01-02', '2025-01-02', 'FAILED', 4),
       (3, 'admin', '2025-01-03', '2025-01-03', 'COMPLETED', 5),
       (4, 'system', '2025-01-04', '2025-01-04', 'FAILED', 6),
       (5, 'admin', '2025-01-05', '2025-01-05', 'IN_PROGRESS', 7),
       (6, 'system', '2025-01-06', '2025-01-06', 'FAILED', 8),
       (7, 'admin', '2025-01-07', '2025-01-07', 'COMPLETED', 9),
       (8, 'system', '2025-01-08', '2025-01-08', 'FAILED', 10),
       (9, 'admin', '2025-01-09', '2025-01-09', 'COMPLETED', 11),
       (10, 'system', '2025-01-10', '2025-01-10', 'FAILED', 12),
       (11, 'admin', '2025-01-11', '2025-01-11', 'COMPLETED', 13),
       (12, 'system', '2025-01-12', '2025-01-12', 'IN_PROGRESS', 14),
       (13, 'admin', '2025-01-13', '2025-01-13', 'COMPLETED', 15),
       (14, 'system', '2025-01-14', '2025-01-14', 'FAILED', 16),
       (15, 'admin', '2025-01-15', '2025-01-15', 'COMPLETED', 17),
       (16, 'system', '2025-01-16', '2025-01-16', 'FAILED', 18),
       (17, 'admin', '2025-01-17', '2025-01-17', 'COMPLETED', 19),
       (18, 'system', '2025-01-18', '2025-01-18', 'IN_PROGRESS', 20),
       (19, 'admin', '2025-01-19', '2025-01-19', 'COMPLETED', 1),
       (20, 'system', '2025-01-20', '2025-01-20', 'FAILED', 2);

-- -- 하위 테이블부터 삭제
DROP TABLE IF EXISTS histories CASCADE;
DROP TABLE IF EXISTS change_logs CASCADE;
DROP TABLE IF EXISTS backups CASCADE;
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS binary_contents CASCADE;
DROP TABLE IF EXISTS departments CASCADE;

CREATE USER hrbank_user WITH PASSWORD 'hrbank_1234';
GRANT ALL PRIVILEGES ON DATABASE hrbank TO hrbank_user;
-- 모든 테이블 권한
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO hrbank_user;

-- 모든 시퀀스 권한
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO hrbank_user;
