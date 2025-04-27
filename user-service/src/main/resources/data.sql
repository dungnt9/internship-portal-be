INSERT INTO students (id, auth_user_id, student_code, name, class_name, major, gender, birthday, address, cpa, english_level, skills, created_at, updated_at) VALUES
(1, 18, '20216805', 'Nguyễn Tiến Dũng', 'KHMT03-K66', 'Khoa học máy tính', 'Male', '2003-05-15', 'Hà Nội', 3.6, 'Intermediate', 'Java, Spring Boot, Vue.js, Docker', NOW(), NOW()),
(2, 19, '20216123', 'Lê Thị Anh', 'KHMT01-K66', 'Khoa học máy tính', 'Female', '2003-03-21', 'Hải Phòng', 3.8, 'Upper Intermediate', 'Java, ReactJS, Python, SQL', NOW(), NOW()),
(3, 20, '20216456', 'Nguyễn Thị Trang', 'KHMT02-K66', 'Khoa học máy tính', 'Female', '2003-08-10', 'Nam Định', 3.5, 'Intermediate', 'C/C++, Java, HTML/CSS, Node.js', NOW(), NOW()),
(4, 21, '20216789', 'Vũ Quang Minh', 'CNTT01-K66', 'Công nghệ thông tin', 'Male', '2003-02-28', 'Thái Bình', 3.7, 'Advanced', 'Python, Django, JavaScript, AWS', NOW(), NOW()),
(5, 22, '20216012', 'Nguyễn Hoàng Sơn', 'CNTT02-K66', 'Công nghệ thông tin', 'Male', '2003-07-22', 'Hà Nội', 3.4, 'Intermediate', 'Java, Spring, Angular, MySQL', NOW(), NOW()),
(6, 23, '20216345', 'Nguyễn Văn Hiền', 'HTTT01-K66', 'Hệ thống thông tin', 'Male', '2003-11-05', 'Bắc Ninh', 3.9, 'Upper Intermediate', 'Java, PHP, React, MongoDB', NOW(), NOW()),
(7, 24, '20216678', 'Nguyễn Văn Thành', 'HTTT02-K66', 'Hệ thống thông tin', 'Male', '2003-01-30', 'Hưng Yên', 3.2, 'Upper Intermediate', 'Java, Python, ReactJS, PostgreSQL', NOW(), NOW()),
(8, 25, '20216901', 'Nguyễn Thị Lam', 'KTPM01-K66', 'Kỹ thuật phần mềm', 'Female', '2003-04-17', 'Hà Nội', 3.6, 'Advanced', 'Java, .NET, Angular, Oracle', NOW(), NOW());

INSERT INTO cv_files (id, student_id, file_name, file_path, file_size, upload_date, created_at, updated_at) VALUES
(1, 1, 'CV_NguyenTienDung_2024.pdf', '/uploads/cv/20216805/CV_NguyenTienDung_2024.pdf', 512, DATE_SUB(NOW(), INTERVAL 30 DAY), NOW(), NOW()),
(2, 1, 'CV_NguyenTienDung_2024_v2.pdf', '/uploads/cv/20216805/CV_NguyenTienDung_2024_v2.pdf', 530, DATE_SUB(NOW(), INTERVAL 15 DAY), NOW(), NOW()),
(3, 2, 'CV_LeThiAnh_2024.pdf', '/uploads/cv/20216123/CV_LeThiAnh_2024.pdf', 498, DATE_SUB(NOW(), INTERVAL 25 DAY), NOW(), NOW()),
(4, 3, 'CV_NguyenThiTrang_2024.pdf', '/uploads/cv/20216456/CV_NguyenThiTrang_2024.pdf', 505, DATE_SUB(NOW(), INTERVAL 20 DAY), NOW(), NOW()),
(5, 4, 'CV_VuQuangMinh_2024.pdf', '/uploads/cv/20216789/CV_VuQuangMinh_2024.pdf', 487, DATE_SUB(NOW(), INTERVAL 22 DAY), NOW(), NOW()),
(6, 5, 'CV_NguyenHoangSon_2024.pdf', '/uploads/cv/20216012/CV_NguyenHoangSon_2024.pdf', 510, DATE_SUB(NOW(), INTERVAL 18 DAY), NOW(), NOW()),
(7, 6, 'CV_NguyenVanHien_2024.pdf', '/uploads/cv/20216345/CV_NguyenVanHien_2024.pdf', 520, DATE_SUB(NOW(), INTERVAL 21 DAY), NOW(), NOW()),
(8, 7, 'CV_NguyenVanThanh_2024.pdf', '/uploads/cv/20216678/CV_NguyenVanThanh_2024.pdf', 495, DATE_SUB(NOW(), INTERVAL 19 DAY), NOW(), NOW()),
(9, 8, 'CV_NguyenThiLam_2024.pdf', '/uploads/cv/20216901/CV_NguyenThiLam_2024.pdf', 502, DATE_SUB(NOW(), INTERVAL 23 DAY), NOW(), NOW());

INSERT INTO companies (id, name, short_name, is_foreign_company, tax_code, website, address, business_type, description, founding_year, employee_count, capital, logo_path, is_verified, is_linked, created_at, updated_at) VALUES
(1, 'FPT Software', 'Công ty TNHH Phần mềm FPT', false, '0101248141', 'https://fptsoftware.com', 'Tòa nhà FPT, Phạm Hùng, Cầu Giấy, Hà Nội', 'Công nghệ thông tin', 'FPT Software là công ty thành viên của Tập đoàn FPT, một trong những công ty công nghệ thông tin hàng đầu Việt Nam.', 1999, 20000, 5000000000, '/uploads/logos/fpt_software.png', true, true, NOW(), NOW()),
(2, 'Viettel', 'Tập đoàn Công nghiệp - Viễn thông Quân đội', false, '0100109106', 'https://viettel.com.vn', 'Số 1 Giang Văn Minh, Kim Mã, Ba Đình, Hà Nội', 'Viễn thông', 'Tập đoàn Công nghiệp - Viễn thông Quân đội (Viettel) là tập đoàn viễn thông lớn nhất Việt Nam.', 1989, 70000, 300000000000, '/uploads/logos/viettel.png', true, true, NOW(), NOW()),
(3, 'VNPT', 'Tập đoàn Bưu chính Viễn thông Việt Nam', false, '0100684378', 'https://vnpt.com.vn', 'Số 57 Huỳnh Thúc Kháng, Láng Hạ, Đống Đa, Hà Nội', 'Viễn thông', 'Tập đoàn Bưu chính Viễn thông Việt Nam (VNPT) là doanh nghiệp nhà nước cung cấp dịch vụ bưu chính viễn thông.', 1995, 50000, 200000000000, '/uploads/logos/vnpt.png', true, true, NOW(), NOW()),
(4, 'MISA', 'Công ty Cổ phần MISA', false, '0101243150', 'https://misa.com.vn', 'Tòa nhà Technosoft, Phố Duy Tân, Cầu Giấy, Hà Nội', 'Phần mềm', 'MISA là công ty phát triển và cung cấp phần mềm quản lý, kế toán hàng đầu tại Việt Nam.', 1994, 2000, 50000000000, '/uploads/logos/misa.png', true, true, NOW(), NOW()),
(5, 'CMC Global', 'Công ty Cổ phần CMC Global', false, '0107267567', 'https://cmcglobal.com.vn', 'Tòa nhà CMC, Duy Tân, Cầu Giấy, Hà Nội', 'Công nghệ thông tin', 'CMC Global là đơn vị thành viên của Tập đoàn Công nghệ CMC, chuyên cung cấp dịch vụ phát triển phần mềm chất lượng cao.', 2017, 1500, 40000000000, '/uploads/logos/cmc_global.png', true, true, NOW(), NOW()),
(6, 'Microsoft Vietnam', 'Microsoft Vietnam LLC', true, NULL, 'https://microsoft.com', 'Tòa nhà Keangnam, Phạm Hùng, Nam Từ Liêm, Hà Nội', 'Công nghệ thông tin', 'Microsoft Vietnam là văn phòng đại diện của tập đoàn Microsoft tại Việt Nam.', 1975, 150000, 1000000000000, '/uploads/logos/microsoft.png', true, true, NOW(), NOW()),
(7, 'Google Vietnam', 'Google Asia Pacific Pte. Ltd.', true, NULL, 'https://google.com', 'Tòa nhà BitexcoFinancial, Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh', 'Công nghệ thông tin', 'Google Vietnam cung cấp các dịch vụ tìm kiếm, quảng cáo và các sản phẩm công nghệ khác.', 1998, 135000, 900000000000, '/uploads/logos/google.png', true, true, NOW(), NOW());

INSERT INTO company_contacts (id, company_id, name, position, auth_user_id, created_at, updated_at) VALUES
(1, 1, 'Nguyễn Thị Hương', 'HR Manager', 9, NOW(), NOW()),
(2, 1, 'Phạm Văn Nam', 'Talent Acquisition', 10, NOW(), NOW()),
(3, 2, 'Trần Văn Hùng', 'HR Director', 11, NOW(), NOW()),
(4, 2, 'Lê Thị Mai', 'Recruiter', 12, NOW(), NOW()),
(5, 3, 'Nguyễn Văn Thắng', 'HR Manager', 13, NOW(), NOW()),
(6, 4, 'Phạm Thị Hà', 'HR Specialist', 14, NOW(), NOW()),
(7, 5, 'Vũ Minh Đức', 'Talent Acquisition Lead', 15, NOW(), NOW()),
(8, 6, 'Nguyễn Văn Toàn', 'HR Director', 16, NOW(), NOW()),
(9, 7, 'Phạm Thị Linh', 'Talent Acquisition Manager', 17, NOW(), NOW());

INSERT INTO teachers (id, auth_user_id, name, department, position, created_at, updated_at) VALUES
(1, 5, 'TS. Vũ Thành Nam', 'Khoa Toán - Tin học', 'Giảng viên', NOW(), NOW()),
(2, 6, 'TS. Nguyễn Trung Hoàng', 'Khoa Toán - Tin học', 'Giảng viên', NOW(), NOW()),
(3, 7, 'PGS.TS. Lê Đình Hưng', 'Khoa Toán - Tin học', 'Phó Trưởng khoa', NOW(), NOW()),
(4, 8, 'ThS. Phạm Văn Cường', 'Khoa Toán - Tin học', 'Giảng viên', NOW(), NOW());

INSERT INTO admins (id, auth_user_id, name, department, position, created_at, updated_at) VALUES
(1, 3, 'Nguyễn Văn Quyết', 'Phòng Đào tạo', 'Trưởng phòng', NOW(), NOW()),
(2, 4, 'Trần Thị Minh', 'Phòng Công tác Sinh viên', 'Chuyên viên', NOW(), NOW());

INSERT INTO notifications (id, auth_user_id, title, content, is_read, type, reference_id, reference_type, created_at, updated_at) VALUES
(1, 18, 'Đăng ký thực tập thành công', 'Bạn đã đăng ký thực tập tại FPT Software thành công', false, 'registration', 1, 'internship', NOW(), NOW()),
(2, 18, 'Thông báo phỏng vấn', 'FPT Software mời bạn tham gia phỏng vấn vào ngày 20/05/2025', false, 'interview', 1, 'company', DATE_SUB(NOW(), INTERVAL 2 DAY), NOW()),
(3, 19, 'Đăng ký thực tập thành công', 'Bạn đã đăng ký thực tập tại Viettel thành công', true, 'registration', 2, 'internship', NOW(), NOW()),
(4, 20, 'Đăng ký thực tập thành công', 'Bạn đã đăng ký thực tập tại VNPT thành công', false, 'registration', 3, 'internship', NOW(), NOW()),
(5, 21, 'Thông báo phỏng vấn', 'MISA mời bạn tham gia phỏng vấn vào ngày 21/05/2025', false, 'interview', 4, 'company', DATE_SUB(NOW(), INTERVAL 1 DAY), NOW()),
(6, 5, 'Sinh viên đăng ký thực tập', 'Có 5 sinh viên mới đã đăng ký thực tập cần được phê duyệt', false, 'approval', 1, 'batch', NOW(), NOW()),
(7, 9, 'Ứng viên mới', 'Có 3 sinh viên mới đã ứng tuyển vào vị trí thực tập tại công ty', false, 'application', 1, 'company', NOW(), NOW());

INSERT INTO news (id, admin_id, title, content, is_published, created_at, updated_at, deleted_at) VALUES
(1, 1, 'Thông báo về kỳ thực tập năm học 2024-2025', 'Kính gửi các bạn sinh viên,\n\nNhà trường thông báo về kỳ thực tập học kỳ 2 năm học 2024-2025 sẽ diễn ra từ ngày 01/05/2025 đến ngày 15/07/2025. Sinh viên cần đăng ký thực tập trước ngày 15/04/2025.', true, DATE_SUB(NOW(), INTERVAL 10 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), NULL),
(2, 1, 'Danh sách các công ty hợp tác thực tập học kỳ 2 năm học 2024-2025', 'Nhà trường công bố danh sách các công ty hợp tác thực tập học kỳ 2 năm học 2024-2025 bao gồm: FPT Software, Viettel, VNPT, MISA, CMC Global, Microsoft Vietnam, Google Vietnam.', true, DATE_SUB(NOW(), INTERVAL 8 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), NULL),
(3, 2, 'Hướng dẫn đăng ký thực tập trên cổng thông tin', 'Nhà trường đã cung cấp cổng thông tin đăng ký thực tập trực tuyến. Sinh viên có thể truy cập và đăng nhập bằng tài khoản đã được cấp. Hướng dẫn chi tiết như sau...', true, DATE_SUB(NOW(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 7 DAY), NULL),
(4, 1, 'Thông báo buổi gặp mặt doanh nghiệp tham gia chương trình thực tập', 'Nhà trường tổ chức buổi gặp mặt với các doanh nghiệp tham gia chương trình thực tập vào ngày 10/04/2025 tại Hội trường C2. Kính mời các công ty và sinh viên tham dự.', true, DATE_SUB(NOW(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 5 DAY), NULL),
(5, 2, 'Chuẩn bị hồ sơ thực tập - CV và các giấy tờ cần thiết', 'Để chuẩn bị tốt cho kỳ thực tập sắp tới, sinh viên cần chuẩn bị các hồ sơ sau: CV cập nhật, bảng điểm, thư giới thiệu (nếu có). Nhà trường sẽ tổ chức workshop hướng dẫn viết CV vào ngày 05/04/2025.', true, DATE_SUB(NOW(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), NULL);