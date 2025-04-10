-- Insert User References
INSERT INTO user_references (id, auth_user_id, role_name) VALUES
                                                              (1, 1, 'ROLE_ADMIN'),
                                                              (2, 2, 'ROLE_ADMIN'),
                                                              (11, 11, 'ROLE_TEACHER'),
                                                              (12, 12, 'ROLE_TEACHER'),
                                                              (13, 13, 'ROLE_TEACHER'),
                                                              (14, 14, 'ROLE_TEACHER'),
                                                              (21, 21, 'ROLE_COMPANY'),
                                                              (22, 22, 'ROLE_COMPANY'),
                                                              (23, 23, 'ROLE_COMPANY'),
                                                              (24, 24, 'ROLE_COMPANY'),
                                                              (25, 25, 'ROLE_COMPANY'),
                                                              (101, 101, 'ROLE_STUDENT'),
                                                              (102, 102, 'ROLE_STUDENT'),
                                                              (103, 103, 'ROLE_STUDENT'),
                                                              (104, 104, 'ROLE_STUDENT'),
                                                              (105, 105, 'ROLE_STUDENT'),
                                                              (106, 106, 'ROLE_STUDENT'),
                                                              (107, 107, 'ROLE_STUDENT'),
                                                              (108, 108, 'ROLE_STUDENT');

-- Insert Students
INSERT INTO students (id, user_ref_id, student_code, full_name, class_name, major, gender, birthday, address, cpa, english_level, skills, created_at, updated_at) VALUES
                                                                                                                                                                      (1, 101, '20216805', 'Nguyễn Tiến Dũng', 'KHMT03-K66', 'Khoa học máy tính', 'Male', '2003-05-15', 'Hà Nội', 3.6, 'Intermediate', 'Java, Spring Boot, Vue.js, Docker', NOW(), NOW()),
                                                                                                                                                                      (2, 102, '20216123', 'Lê Thị Anh', 'KHMT01-K66', 'Khoa học máy tính', 'Female', '2003-03-21', 'Hải Phòng', 3.8, 'Upper Intermediate', 'Java, ReactJS, Python, SQL', NOW(), NOW()),
                                                                                                                                                                      (3, 103, '20216456', 'Nguyễn Thị Trang', 'KHMT02-K66', 'Khoa học máy tính', 'Female', '2003-08-10', 'Nam Định', 3.5, 'Intermediate', 'C/C++, Java, HTML/CSS, Node.js', NOW(), NOW()),
                                                                                                                                                                      (4, 104, '20216789', 'Vũ Quang Minh', 'CNTT01-K66', 'Công nghệ thông tin', 'Male', '2003-02-28', 'Thái Bình', 3.7, 'Advanced', 'Python, Django, JavaScript, AWS', NOW(), NOW()),
                                                                                                                                                                      (5, 105, '20216012', 'Nguyễn Hoàng Sơn', 'CNTT02-K66', 'Công nghệ thông tin', 'Male', '2003-07-22', 'Hà Nội', 3.4, 'Intermediate', 'Java, Spring, Angular, MySQL', NOW(), NOW()),
                                                                                                                                                                      (6, 106, '20216345', 'Nguyễn Văn Hiền', 'HTTT01-K66', 'Hệ thống thông tin', 'Male', '2003-11-05', 'Bắc Ninh', 3.9, 'Upper Intermediate', 'Java, PHP, React, MongoDB', NOW(), NOW()),
                                                                                                                                                                      (7, 107, '20216678', 'Nguyễn Văn Thành', 'HTTT02-K66', 'Hệ thống thông tin', 'Male', '2003-01-30', 'Hưng Yên', 3.2, 'Upper Intermediate', 'Java, Python, ReactJS, PostgreSQL', NOW(), NOW()),
                                                                                                                                                                      (8, 108, '20216901', 'Nguyễn Thị Lam', 'KTPM01-K66', 'Kỹ thuật phần mềm', 'Female', '2003-04-17', 'Hà Nội', 3.6, 'Advanced', 'Java, .NET, Angular, Oracle', NOW(), NOW());

-- Insert CV Files
INSERT INTO cv_files (id, student_id, file_name, file_path, file_size, upload_date) VALUES
                                                                                        (1, 1, 'CV_NguyenTienDung_2024.pdf', '/uploads/cv/20216805/CV_NguyenTienDung_2024.pdf', 512, DATE_SUB(NOW(), INTERVAL 30 DAY)),
                                                                                        (2, 1, 'CV_NguyenTienDung_2024_v2.pdf', '/uploads/cv/20216805/CV_NguyenTienDung_2024_v2.pdf', 530, DATE_SUB(NOW(), INTERVAL 15 DAY)),
                                                                                        (3, 2, 'CV_LeThiAnh_2024.pdf', '/uploads/cv/20216123/CV_LeThiAnh_2024.pdf', 498, DATE_SUB(NOW(), INTERVAL 25 DAY)),
                                                                                        (4, 3, 'CV_NguyenThiTrang_2024.pdf', '/uploads/cv/20216456/CV_NguyenThiTrang_2024.pdf', 505, DATE_SUB(NOW(), INTERVAL 20 DAY)),
                                                                                        (5, 4, 'CV_VuQuangMinh_2024.pdf', '/uploads/cv/20216789/CV_VuQuangMinh_2024.pdf', 487, DATE_SUB(NOW(), INTERVAL 22 DAY)),
                                                                                        (6, 5, 'CV_NguyenHoangSon_2024.pdf', '/uploads/cv/20216012/CV_NguyenHoangSon_2024.pdf', 510, DATE_SUB(NOW(), INTERVAL 18 DAY)),
                                                                                        (7, 6, 'CV_NguyenVanHien_2024.pdf', '/uploads/cv/20216345/CV_NguyenVanHien_2024.pdf', 520, DATE_SUB(NOW(), INTERVAL 21 DAY)),
                                                                                        (8, 7, 'CV_NguyenVanThanh_2024.pdf', '/uploads/cv/20216678/CV_NguyenVanThanh_2024.pdf', 495, DATE_SUB(NOW(), INTERVAL 19 DAY)),
                                                                                        (9, 8, 'CV_NguyenThiLam_2024.pdf', '/uploads/cv/20216901/CV_NguyenThiLam_2024.pdf', 502, DATE_SUB(NOW(), INTERVAL 23 DAY));

-- Insert Companies
INSERT INTO companies (id, user_ref_id, name, display_name, tax_code, website, address, business_type, description, founding_year, employee_count, capital, logo_path, is_verified, is_linked, created_at, updated_at) VALUES
                                                                                                                                                                                                                           (1, 21, 'FPT Software', 'Công ty TNHH Phần mềm FPT', '0101248141', 'https://fptsoftware.com', 'Tòa nhà FPT, Phạm Hùng, Cầu Giấy, Hà Nội', 'Công nghệ thông tin', 'FPT Software là công ty thành viên của Tập đoàn FPT, một trong những công ty công nghệ thông tin hàng đầu Việt Nam.', 1999, 20000, 5000000000, '/uploads/logos/fpt_software.png', true, true, NOW(), NOW()),
                                                                                                                                                                                                                           (2, 22, 'Viettel', 'Tập đoàn Công nghiệp - Viễn thông Quân đội', '0100109106', 'https://viettel.com.vn', 'Số 1 Giang Văn Minh, Kim Mã, Ba Đình, Hà Nội', 'Viễn thông', 'Tập đoàn Công nghiệp - Viễn thông Quân đội (Viettel) là tập đoàn viễn thông lớn nhất Việt Nam.', 1989, 70000, 300000000000, '/uploads/logos/viettel.png', true, true, NOW(), NOW()),
                                                                                                                                                                                                                           (3, 23, 'VNPT', 'Tập đoàn Bưu chính Viễn thông Việt Nam', '0100684378', 'https://vnpt.com.vn', 'Số 57 Huỳnh Thúc Kháng, Láng Hạ, Đống Đa, Hà Nội', 'Viễn thông', 'Tập đoàn Bưu chính Viễn thông Việt Nam (VNPT) là doanh nghiệp nhà nước cung cấp dịch vụ bưu chính viễn thông.', 1995, 50000, 200000000000, '/uploads/logos/vnpt.png', true, true, NOW(), NOW()),
                                                                                                                                                                                                                           (4, 24, 'MISA', 'Công ty Cổ phần MISA', '0101243150', 'https://misa.com.vn', 'Tòa nhà Technosoft, Phố Duy Tân, Cầu Giấy, Hà Nội', 'Phần mềm', 'MISA là công ty phát triển và cung cấp phần mềm quản lý, kế toán hàng đầu tại Việt Nam.', 1994, 2000, 50000000000, '/uploads/logos/misa.png', true, true, NOW(), NOW()),
                                                                                                                                                                                                                           (5, 25, 'CMC Global', 'Công ty Cổ phần CMC Global', '0107267567', 'https://cmcglobal.com.vn', 'Tòa nhà CMC, Duy Tân, Cầu Giấy, Hà Nội', 'Công nghệ thông tin', 'CMC Global là đơn vị thành viên của Tập đoàn Công nghệ CMC, chuyên cung cấp dịch vụ phát triển phần mềm chất lượng cao.', 2017, 1500, 40000000000, '/uploads/logos/cmc_global.png', true, true, NOW(), NOW());

-- Insert Company Contacts
INSERT INTO company_contacts (id, company_id, name, position, email, phone, created_at, updated_at) VALUES
                                                                                                        (1, 1, 'Nguyễn Thị Hương', 'HR Manager', 'huong.nt@fpt.com.vn', '0912345678', NOW(), NOW()),
                                                                                                        (2, 1, 'Phạm Văn Nam', 'Talent Acquisition', 'nam.pv@fpt.com.vn', '0912345679', NOW(), NOW()),
                                                                                                        (3, 2, 'Trần Văn Hùng', 'HR Director', 'hung.tv@viettel.com.vn', '0912345680', NOW(), NOW()),
                                                                                                        (4, 2, 'Lê Thị Mai', 'Recruiter', 'mai.lt@viettel.com.vn', '0912345681', NOW(), NOW()),
                                                                                                        (5, 3, 'Nguyễn Văn Thắng', 'HR Manager', 'thang.nv@vnpt.com.vn', '0912345682', NOW(), NOW()),
                                                                                                        (6, 4, 'Phạm Thị Hà', 'HR Specialist', 'ha.pt@misa.com.vn', '0912345683', NOW(), NOW()),
                                                                                                        (7, 5, 'Vũ Minh Đức', 'Talent Acquisition Lead', 'duc.vm@cmcglobal.vn', '0912345684', NOW(), NOW());

-- Insert Teachers
INSERT INTO teachers (id, user_ref_id, full_name, department, position, created_at, updated_at) VALUES
                                                                                                    (1, 11, 'TS. Vũ Thành Nam', 'Khoa Toán - Tin học', 'Giảng viên', NOW(), NOW()),
                                                                                                    (2, 12, 'TS. Nguyễn Trung Hoàng', 'Khoa Toán - Tin học', 'Giảng viên', NOW(), NOW()),
                                                                                                    (3, 13, 'PGS.TS. Lê Đình Hưng', 'Khoa Toán - Tin học', 'Phó Trưởng khoa', NOW(), NOW()),
                                                                                                    (4, 14, 'ThS. Phạm Văn Cường', 'Khoa Toán - Tin học', 'Giảng viên', NOW(), NOW());

-- Insert Admins
INSERT INTO admins (id, user_ref_id, full_name, department, position, created_at, updated_at) VALUES
                                                                                                  (1, 1, 'Nguyễn Văn Quyết', 'Phòng Đào tạo', 'Trưởng phòng', NOW(), NOW()),
                                                                                                  (2, 2, 'Trần Thị Minh', 'Phòng Công tác Sinh viên', 'Chuyên viên', NOW(), NOW());

-- Insert Notifications
INSERT INTO notifications (id, user_ref_id, title, content, is_read, type, reference_id, reference_type, created_at) VALUES
                                                                                                                         (1, 101, 'Đăng ký thực tập thành công', 'Bạn đã đăng ký thực tập tại FPT Software thành công', false, 'registration', 1, 'internship', NOW()),
                                                                                                                         (2, 101, 'Thông báo phỏng vấn', 'FPT Software mời bạn tham gia phỏng vấn vào ngày 20/05/2025', false, 'interview', 1, 'company', DATE_SUB(NOW(), INTERVAL 2 DAY)),
                                                                                                                         (3, 102, 'Đăng ký thực tập thành công', 'Bạn đã đăng ký thực tập tại Viettel thành công', true, 'registration', 2, 'internship', NOW()),
                                                                                                                         (4, 103, 'Đăng ký thực tập thành công', 'Bạn đã đăng ký thực tập tại VNPT thành công', false, 'registration', 3, 'internship', NOW()),
                                                                                                                         (5, 104, 'Thông báo phỏng vấn', 'MISA mời bạn tham gia phỏng vấn vào ngày 21/05/2025', false, 'interview', 4, 'company', DATE_SUB(NOW(), INTERVAL 1 DAY)),
                                                                                                                         (6, 11, 'Sinh viên đăng ký thực tập', 'Có 5 sinh viên mới đã đăng ký thực tập cần được phê duyệt', false, 'approval', 1, 'batch', NOW()),
                                                                                                                         (7, 21, 'Ứng viên mới', 'Có 3 sinh viên mới đã ứng tuyển vào vị trí thực tập tại công ty', false, 'application', 1, 'company', NOW());