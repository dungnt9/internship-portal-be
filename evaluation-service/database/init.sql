-- CREATE DATABASE evaluation_service;
USE evaluation_service;

-- Reference tables to link to other services
CREATE TABLE progress_references (
    id INT AUTO_INCREMENT PRIMARY KEY,
    original_progress_id INT NOT NULL UNIQUE,
    student_name VARCHAR(100) NOT NULL,
    period_name VARCHAR(100) NOT NULL,
    position_title VARCHAR(100),
    company_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE internship_reports (
    id INT AUTO_INCREMENT PRIMARY KEY,
    progress_ref_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    file_path VARCHAR(255),
    submission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (progress_ref_id) REFERENCES progress_references(id) ON DELETE CASCADE
);

CREATE TABLE evaluation_criteria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE company_evaluations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    progress_ref_id INT NOT NULL,
    evaluation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    overall_score DECIMAL(4,2),
    comments TEXT,
    FOREIGN KEY (progress_ref_id) REFERENCES progress_references(id) ON DELETE CASCADE,
    UNIQUE KEY (progress_ref_id)
);

CREATE TABLE company_evaluation_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    evaluation_id INT NOT NULL,
    criteria_id INT NOT NULL,
    score DECIMAL(4,2),
    comments TEXT,
    FOREIGN KEY (evaluation_id) REFERENCES company_evaluations(id) ON DELETE CASCADE,
    FOREIGN KEY (criteria_id) REFERENCES evaluation_criteria(id),
    UNIQUE KEY (evaluation_id, criteria_id)
);