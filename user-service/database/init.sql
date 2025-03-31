CREATE DATABASE user_service;
USE user_service;

-- Reference to user IDs in auth service
CREATE TABLE user_references (
    id INT AUTO_INCREMENT PRIMARY KEY,
    auth_user_id INT NOT NULL UNIQUE,
    role_name VARCHAR(50) NOT NULL
);

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_ref_id INT NOT NULL UNIQUE,
    student_code VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    class_name VARCHAR(50),
    major VARCHAR(100),
    gender ENUM('Nam', 'Nữ', 'Khác'),
    birthday DATE,
    address VARCHAR(255),
    cpa DECIMAL(3,2),
    english_level VARCHAR(100),
    skills TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_ref_id) REFERENCES user_references(id) ON DELETE CASCADE
);

CREATE TABLE cv_files (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    file_size INT,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE companies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_ref_id INT NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    display_name VARCHAR(150),
    tax_code VARCHAR(20),
    website VARCHAR(255),
    address VARCHAR(255),
    business_type VARCHAR(100),
    description TEXT,
    founding_year INT,
    employee_count INT,
    capital DECIMAL(20,2),
    logo_path VARCHAR(255),
    is_verified BOOLEAN DEFAULT FALSE,
    is_linked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_ref_id) REFERENCES user_references(id) ON DELETE CASCADE
);

CREATE TABLE company_contacts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    position VARCHAR(100),
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE
);

CREATE TABLE teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_ref_id INT NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    position VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_ref_id) REFERENCES user_references(id) ON DELETE CASCADE
);

CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_ref_id INT NOT NULL UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    position VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_ref_id) REFERENCES user_references(id) ON DELETE CASCADE
);

-- Notifications for users
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_ref_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    type VARCHAR(50),
    reference_id INT,
    reference_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_ref_id) REFERENCES user_references(id) ON DELETE CASCADE
);