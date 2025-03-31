CREATE DATABASE register_service;
USE register_service;

-- Reference tables to link to other services
CREATE TABLE student_references (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL UNIQUE,
    student_code VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL
);

CREATE TABLE company_references (
    id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE teacher_references (
    id INT AUTO_INCREMENT PRIMARY KEY,
    teacher_id INT NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL
);

-- Main tables
CREATE TABLE internship_periods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    registration_start_date DATE NOT NULL,
    registration_end_date DATE NOT NULL,
    status ENUM('Chưa bắt đầu', 'Đang diễn ra', 'Kết thúc') DEFAULT 'Chưa bắt đầu',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE internship_positions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    company_ref_id INT NOT NULL,
    period_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    requirements TEXT,
    benefits TEXT,
    available_slots INT NOT NULL DEFAULT 1,
    registered_count INT NOT NULL DEFAULT 0,
    work_type ENUM('Full-time', 'Part-time') DEFAULT 'Full-time',
    due_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (company_ref_id) REFERENCES company_references(id) ON DELETE CASCADE,
    FOREIGN KEY (period_id) REFERENCES internship_periods(id) ON DELETE CASCADE
);

CREATE TABLE cv_references (
    id INT AUTO_INCREMENT PRIMARY KEY,
    original_cv_id INT NOT NULL,
    student_ref_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    upload_date TIMESTAMP,
    FOREIGN KEY (student_ref_id) REFERENCES student_references(id) ON DELETE CASCADE
);

CREATE TABLE internship_applications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_ref_id INT NOT NULL,
    position_id INT NOT NULL,
    period_id INT NOT NULL,
    preference_order INT NOT NULL,
    status ENUM('Pending', 'Approved', 'Rejected', 'Cancelled') DEFAULT 'Pending',
    applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cv_ref_id INT,
    note TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_ref_id) REFERENCES student_references(id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES internship_positions(id) ON DELETE CASCADE,
    FOREIGN KEY (period_id) REFERENCES internship_periods(id) ON DELETE CASCADE,
    FOREIGN KEY (cv_ref_id) REFERENCES cv_references(id),
    UNIQUE KEY (student_ref_id, position_id, period_id)
);

CREATE TABLE external_internships (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_ref_id INT NOT NULL,
    period_id INT NOT NULL,
    company_name VARCHAR(100) NOT NULL,
    company_address VARCHAR(255),
    position VARCHAR(100) NOT NULL,

    supervisor_name VARCHAR(100),
    supervisor_position VARCHAR(100),
    supervisor_email VARCHAR(100),
    supervisor_phone VARCHAR(20),

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    work_schedule TEXT,
    confirmation_file_path VARCHAR(255), -- Giấy xác nhận thực tập
    status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending',
    approval_date TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_ref_id) REFERENCES student_references(id) ON DELETE CASCADE,
    FOREIGN KEY (period_id) REFERENCES internship_periods(id) ON DELETE CASCADE
);

CREATE TABLE internship_progress (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_ref_id INT NOT NULL,
    position_id INT,
    period_id INT NOT NULL,
    teacher_ref_id INT,
    start_date DATE,
    end_date DATE,
    is_external BOOLEAN DEFAULT FALSE,
    external_id INT,
    status ENUM('Assigned', 'In Progress', 'Completed', 'Cancelled') DEFAULT 'Assigned',

    -- Thông tin cán bộ hướng dẫn tại doanh nghiệp
    supervisor_name VARCHAR(100),
    supervisor_position VARCHAR(100),
    supervisor_email VARCHAR(100),
    supervisor_phone VARCHAR(20),

    teacher_confirmed BOOLEAN DEFAULT FALSE,
    teacher_confirmed_at TIMESTAMP NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_ref_id) REFERENCES student_references(id) ON DELETE CASCADE,
    FOREIGN KEY (position_id) REFERENCES internship_positions(id) ON DELETE SET NULL,
    FOREIGN KEY (period_id) REFERENCES internship_periods(id) ON DELETE CASCADE,
    FOREIGN KEY (teacher_ref_id) REFERENCES teacher_references(id) ON DELETE SET NULL,
    FOREIGN KEY (external_id) REFERENCES external_internships(id) ON DELETE SET NULL,
    UNIQUE KEY (student_ref_id, period_id)
);