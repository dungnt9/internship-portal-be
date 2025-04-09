-- Insert roles
INSERT INTO roles (id, name, description, created_at, updated_at) VALUES
                                                                      (1, 'ROLE_ADMIN', 'Quản trị viên hệ thống', NOW(), NOW()),
                                                                      (2, 'ROLE_TEACHER', 'Giảng viên hướng dẫn', NOW(), NOW()),
                                                                      (3, 'ROLE_COMPANY', 'Doanh nghiệp', NOW(), NOW()),
                                                                      (4, 'ROLE_STUDENT', 'Sinh viên', NOW(), NOW());

-- Insert users for Admin
INSERT INTO users (id, username, email, password, phone, role_id, is_active, created_at, updated_at) VALUES
                                                                                                         (1, 'admin01', 'admin01@hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0987654321', 1, true, NOW(), NOW()),
                                                                                                         (2, 'admin02', 'admin02@hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0987654322', 1, true, NOW(), NOW());

-- Insert users for Teacher
INSERT INTO users (id, username, email, password, phone, role_id, is_active, created_at, updated_at) VALUES
                                                                                                         (11, 'nampv', 'nam.vt@hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912345678', 2, true, NOW(), NOW()),
                                                                                                         (12, 'hoangnt', 'hoang.nt@hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912345679', 2, true, NOW(), NOW()),
                                                                                                         (13, 'hungld', 'hung.ld@hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912345680', 2, true, NOW(), NOW()),
                                                                                                         (14, 'cuongpv', 'cuong.pv@hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912345681', 2, true, NOW(), NOW());

-- Insert users for Company
INSERT INTO users (id, username, email, password, phone, role_id, is_active, created_at, updated_at) VALUES
                                                                                                         (21, 'fpt_software', 'hr@fpt-software.com', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0983456789', 3, true, NOW(), NOW()),
                                                                                                         (22, 'viettel', 'hr@viettel.com.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0983456790', 3, true, NOW(), NOW()),
                                                                                                         (23, 'vnpt', 'hr@vnpt.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0983456791', 3, true, NOW(), NOW()),
                                                                                                         (24, 'misa', 'hr@misa.com.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0983456792', 3, true, NOW(), NOW()),
                                                                                                         (25, 'cmc_global', 'hr@cmcglobal.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0983456793', 3, true, NOW(), NOW());

-- Insert users for Student
INSERT INTO users (id, username, email, password, phone, role_id, is_active, created_at, updated_at) VALUES
                                                                                                         (101, 'dungnt216805', 'dung.nt216805@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876543', 4, true, NOW(), NOW()),
                                                                                                         (102, 'anhlt216123', 'anh.lt216123@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876544', 4, true, NOW(), NOW()),
                                                                                                         (103, 'trangnt216456', 'trang.nt216456@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876545', 4, true, NOW(), NOW()),
                                                                                                         (104, 'minhvq216789', 'minh.vq216789@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876546', 4, true, NOW(), NOW()),
                                                                                                         (105, 'sonnh216012', 'son.nh216012@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876547', 4, true, NOW(), NOW()),
                                                                                                         (106, 'hiennv216345', 'hien.nv216345@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876548', 4, true, NOW(), NOW()),
                                                                                                         (107, 'thanhnv216678', 'thanh.nv216678@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876549', 4, true, NOW(), NOW()),
                                                                                                         (108, 'lamnt216901', 'lam.nt216901@sis.hust.edu.vn', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KTs.O', '0912876550', 4, true, NOW(), NOW());