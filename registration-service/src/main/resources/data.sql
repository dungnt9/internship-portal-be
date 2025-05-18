-- Updated Internship Periods Table with new dates
INSERT INTO internship_periods (id, start_date, end_date, registration_start_date, registration_end_date, status, description, created_at, updated_at, deleted_at) VALUES
('2024.2', '2025-02-10', '2025-06-08', '2024-11-10', '2025-02-09', 'ACTIVE', 'Kỳ thực tập học kỳ 2 năm học 2024-2025', NOW(), NOW(), NULL),
('2025.1', '2025-09-09', '2026-01-12', '2025-05-09', '2025-09-08', 'UPCOMING', 'Kỳ thực tập học kỳ 1 năm học 2025-2026', NOW(), NOW(), NULL);

-- Internship Positions for 2024.2 period
INSERT INTO internship_positions (id, company_id, period_id, title, description, requirements, benefits, available_slots, work_type, status, due_date, created_at, updated_at, deleted_at) VALUES
(1, 1, '2024.2', 'Java Developer Intern', 'Tham gia phát triển các ứng dụng web sử dụng Java Spring và Vue.js', 'Sinh viên năm cuối ngành CNTT, có kiến thức cơ bản về Java, HTML/CSS, JavaScript', 'Được hướng dẫn bởi các kỹ sư có kinh nghiệm, có cơ hội làm việc chính thức sau khi tốt nghiệp, trợ cấp 5 triệu/tháng', 5, 'FULL_TIME', 'OPEN', '2025-02-08', NOW(), NOW(), NULL),
(2, 1, '2024.2', 'Frontend Developer Intern', 'Tham gia phát triển giao diện người dùng cho các ứng dụng web và mobile', 'Sinh viên năm cuối ngành CNTT, có kiến thức về HTML/CSS, JavaScript, ReactJS hoặc Vue.js', 'Được hướng dẫn bởi các kỹ sư có kinh nghiệm, có cơ hội làm việc chính thức sau khi tốt nghiệp, trợ cấp 4.5 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-02-08', NOW(), NOW(), NULL),
(3, 1, '2024.2', 'DevOps Engineer Intern', 'Tham gia vận hành và phát triển hạ tầng CI/CD cho các dự án', 'Sinh viên năm cuối ngành CNTT, có kiến thức về Linux, Docker, hiểu biết cơ bản về cloud computing', 'Được làm việc với công nghệ hiện đại, cơ hội học hỏi từ đội ngũ DevOps chuyên nghiệp, trợ cấp 5 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-02-07', NOW(), NOW(), NULL),
(4, 2, '2024.2', 'Software Engineer Intern', 'Tham gia phát triển các sản phẩm phần mềm trong lĩnh vực viễn thông', 'Sinh viên năm cuối ngành CNTT hoặc Điện tử Viễn thông, có kiến thức về lập trình C/C++, Java', 'Được làm việc trong môi trường chuyên nghiệp, có cơ hội trải nghiệm các công nghệ mới, trợ cấp 5 triệu/tháng', 4, 'FULL_TIME', 'OPEN', '2025-02-05', NOW(), NOW(), NULL),
(5, 2, '2024.2', 'Mobile Developer Intern', 'Tham gia phát triển ứng dụng di động cho các sản phẩm của Viettel', 'Sinh viên năm cuối ngành CNTT, có kiến thức về Java, Kotlin hoặc Swift', 'Được đào tạo về phát triển ứng dụng di động, trợ cấp 5.5 triệu/tháng', 3, 'FULL_TIME', 'OPEN', '2025-02-07', NOW(), NOW(), NULL),
(6, 3, '2024.2', 'Network Engineer Intern', 'Hỗ trợ đội ngũ kỹ thuật trong việc vận hành và phát triển hệ thống mạng', 'Sinh viên năm cuối ngành CNTT hoặc Điện tử Viễn thông, có kiến thức về mạng máy tính, TCP/IP', 'Được đào tạo về các công nghệ mạng mới nhất, tiếp xúc với hệ thống mạng quy mô lớn, trợ cấp 4 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-02-05', NOW(), NOW(), NULL),
(7, 4, '2024.2', 'Software Tester Intern', 'Tham gia đội ngũ kiểm thử phần mềm, phát triển và thực hiện các kịch bản kiểm thử', 'Sinh viên năm cuối ngành CNTT, có kiến thức cơ bản về quy trình phát triển phần mềm, có tư duy logic tốt', 'Được đào tạo về kiểm thử phần mềm, làm việc trong môi trường chuyên nghiệp, trợ cấp 4 triệu/tháng', 3, 'FULL_TIME', 'OPEN', '2025-02-08', NOW(), NOW(), NULL),
(8, 4, '2024.2', 'DevOps Intern', 'Tham gia vào quy trình CI/CD và quản lý hạ tầng', 'Sinh viên năm cuối ngành CNTT, có kiến thức về Docker, Kubernetes và CI/CD', 'Được làm việc với các công nghệ hiện đại, trợ cấp 6.5 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-02-07', NOW(), NOW(), NULL),
(9, 5, '2024.2', 'Data Engineer Intern', 'Tham gia vào việc thu thập, xử lý và phân tích dữ liệu lớn', 'Sinh viên năm cuối ngành CNTT hoặc Toán Tin, có kiến thức về SQL, Python, và các công cụ xử lý dữ liệu', 'Được làm việc với dữ liệu thực tế quy mô lớn, được hướng dẫn bởi các chuyên gia dữ liệu, trợ cấp 5.5 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-02-06', NOW(), NOW(), NULL),
(10, 5, '2024.2', 'Business Analyst Intern', 'Phân tích yêu cầu nghiệp vụ và làm cầu nối giữa khách hàng và đội phát triển', 'Sinh viên năm cuối ngành CNTT hoặc Quản trị kinh doanh, kỹ năng giao tiếp tốt', 'Được học hỏi về quy trình phân tích nghiệp vụ, trợ cấp 4.8 triệu/tháng', 3, 'FULL_TIME', 'OPEN', '2025-02-08', NOW(), NOW(), NULL),
(11, 6, '2024.2', 'Cloud Engineer Intern', 'Hỗ trợ đội ngũ kỹ thuật trong việc triển khai và quản lý các dịch vụ đám mây', 'Sinh viên năm cuối ngành CNTT, có kiến thức về cloud computing, hiểu biết cơ bản về AWS hoặc Azure', 'Được làm việc với các công nghệ đám mây hiện đại, được đào tạo về các dịch vụ Microsoft, trợ cấp 6 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-02-05', NOW(), NOW(), NULL),
(12, 7, '2024.2', 'Machine Learning Intern', 'Tham gia vào các dự án ứng dụng machine learning trong các sản phẩm của Google', 'Sinh viên năm cuối ngành CNTT hoặc Toán Tin, có kiến thức về machine learning, Python, và TensorFlow', 'Được làm việc với các kỹ sư hàng đầu, tiếp xúc với dữ liệu thực tế quy mô lớn, trợ cấp 7 triệu/tháng', 1, 'FULL_TIME', 'OPEN', '2025-02-08', NOW(), NOW(), NULL);

-- Internship Positions for 2025.1 period
INSERT INTO internship_positions (id, company_id, period_id, title, description, requirements, benefits, available_slots, work_type, status, due_date, created_at, updated_at, deleted_at) VALUES
(13, 1, '2025.1', 'Java Developer Intern', 'Tham gia phát triển các ứng dụng doanh nghiệp với Java Spring Boot và PostgreSQL', 'Sinh viên năm cuối ngành CNTT, có kiến thức cơ bản về Java, Spring, cơ sở dữ liệu', 'Được hướng dẫn bởi các kỹ sư có kinh nghiệm, trợ cấp 6 triệu/tháng, có cơ hội nhận việc sau tốt nghiệp', 4, 'FULL_TIME', 'OPEN', '2025-09-07', NOW(), NOW(), NULL),
(14, 1, '2025.1', 'React Developer Intern', 'Tham gia phát triển giao diện người dùng với React và Redux', 'Sinh viên năm cuối ngành CNTT, có kiến thức về HTML/CSS, JavaScript, React', 'Được hướng dẫn bởi các kỹ sư Frontend chuyên nghiệp, trợ cấp 5.5 triệu/tháng', 3, 'FULL_TIME', 'OPEN', '2025-09-07', NOW(), NOW(), NULL),
(15, 2, '2025.1', 'AI Engineer Intern', 'Tham gia phát triển các giải pháp AI cho các sản phẩm viễn thông', 'Sinh viên năm cuối ngành CNTT hoặc Toán tin, có kiến thức về Machine Learning và Python', 'Được làm việc với các dự án có tính thực tế cao, trợ cấp 6 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-09-05', NOW(), NOW(), NULL),
(16, 2, '2025.1', 'Cyber Security Intern', 'Tham gia vào các hoạt động phân tích bảo mật và phòng chống tấn công mạng', 'Sinh viên năm cuối ngành CNTT, có kiến thức về bảo mật mạng, hiểu biết về các lỗ hổng bảo mật', 'Được đào tạo từ các chuyên gia bảo mật, trợ cấp 6.5 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-09-05', NOW(), NOW(), NULL),
(17, 3, '2025.1', 'Data Analyst Intern', 'Phân tích dữ liệu khách hàng và đề xuất giải pháp cải thiện dịch vụ', 'Sinh viên năm cuối ngành CNTT hoặc Thống kê, có kiến thức về SQL, Excel, Power BI', 'Được tiếp xúc với dữ liệu lớn thực tế, trợ cấp 5 triệu/tháng', 3, 'FULL_TIME', 'OPEN', '2025-09-06', NOW(), NOW(), NULL),
(18, 4, '2025.1', 'Backend Developer Intern', 'Phát triển các API và dịch vụ backend cho hệ thống kế toán', 'Sinh viên năm cuối ngành CNTT, kiến thức về .NET hoặc Java Spring', 'Được học hỏi về thiết kế hệ thống, trợ cấp 5.5 triệu/tháng', 4, 'FULL_TIME', 'OPEN', '2025-09-05', NOW(), NOW(), NULL),
(19, 5, '2025.1', 'QA Automation Intern', 'Phát triển và thực hiện các kịch bản kiểm thử tự động', 'Sinh viên năm cuối ngành CNTT, có kiến thức cơ bản về kiểm thử và lập trình', 'Được đào tạo về các công cụ kiểm thử tự động hiện đại, trợ cấp 5 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-09-07', NOW(), NOW(), NULL),
(20, 6, '2025.1', 'Azure Cloud Engineer Intern', 'Hỗ trợ triển khai và quản lý các dịch vụ trên nền tảng Microsoft Azure', 'Sinh viên năm cuối ngành CNTT, có kiến thức về Cloud Computing', 'Được làm việc với công nghệ tiên tiến, trợ cấp 6.5 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-09-06', NOW(), NOW(), NULL),
(21, 7, '2025.1', 'Android Developer Intern', 'Phát triển các ứng dụng Android cho các sản phẩm của Google', 'Sinh viên năm cuối ngành CNTT, có kiến thức về Java/Kotlin và phát triển ứng dụng Android', 'Được làm việc với các kỹ sư của Google, trợ cấp 7 triệu/tháng', 2, 'FULL_TIME', 'OPEN', '2025-09-07', NOW(), NOW(), NULL),
(22, 7, '2025.1', 'UI/UX Design Intern', 'Thiết kế giao diện người dùng và trải nghiệm người dùng cho các sản phẩm của Google', 'Sinh viên ngành CNTT hoặc Thiết kế, có kiến thức về UI/UX và các công cụ thiết kế', 'Được làm việc với đội ngũ thiết kế chuyên nghiệp, trợ cấp 6.5 triệu/tháng', 1, 'FULL_TIME', 'OPEN', '2025-09-05', NOW(), NOW(), NULL);

-- Internship Applications for 2024.2 period with 3 preferences per student
INSERT INTO internship_applications (id, student_id, period_id, cv_file_path, created_at, updated_at, deleted_at) VALUES
(1, 1, '2024.2', '/uploads/cv/20216805/CV_NguyenTienDung_2025.pdf', DATE_SUB(NOW(), INTERVAL 60 DAY), NOW(), NULL),
(2, 2, '2024.2', '/uploads/cv/20216123/CV_Dungntelcom_2025.pdf', DATE_SUB(NOW(), INTERVAL 58 DAY), NOW(), NULL),
(3, 3, '2024.2', '/uploads/cv/20216456/CV_NguyenThiTrang_2025.pdf', DATE_SUB(NOW(), INTERVAL 55 DAY), NOW(), NULL),
(4, 4, '2024.2', '/uploads/cv/20216789/CV_VuQuangMinh_2025.pdf', DATE_SUB(NOW(), INTERVAL 57 DAY), NOW(), NULL),
(5, 5, '2024.2', '/uploads/cv/20216012/CV_NguyenHoangSon_2025.pdf', DATE_SUB(NOW(), INTERVAL 56 DAY), NOW(), NULL);

-- Internship Application Details with 3 preferences per student
INSERT INTO internship_application_details (id, application_id, position_id, preference_order, status, note, created_at, updated_at, deleted_at) VALUES
-- Student 1 (Nguyễn Tiến Dũng) preferences
(1, 1, 1, 1, 'APPROVED', 'Em rất mong muốn được thực tập tại FPT Software để phát triển kỹ năng lập trình Java', DATE_SUB(NOW(), INTERVAL 60 DAY), NOW(), NULL),
(2, 1, 2, 2, 'CANCELLED', 'Em cũng quan tâm đến vị trí Frontend Developer', DATE_SUB(NOW(), INTERVAL 60 DAY), NOW(), NULL),
(3, 1, 11, 3, 'PENDING', 'Em mong muốn học hỏi về Cloud Computing', DATE_SUB(NOW(), INTERVAL 60 DAY), NOW(), NULL),

-- Student 2 (Dungntelcom) preferences
(4, 2, 4, 1, 'APPROVED', 'Em mong muốn được thực tập tại Viettel để học hỏi thêm về phát triển phần mềm trong môi trường doanh nghiệp lớn', DATE_SUB(NOW(), INTERVAL 58 DAY), NOW(), NULL),
(5, 2, 5, 2, 'CANCELLED', 'Em có kinh nghiệm với phát triển ứng dụng di động', DATE_SUB(NOW(), INTERVAL 58 DAY), NOW(), NULL),
(6, 2, 1, 3, 'PENDING', 'Em cũng có kinh nghiệm với Java Spring Boot', DATE_SUB(NOW(), INTERVAL 58 DAY), NOW(), NULL),

-- Student 3 (Nguyễn Thị Trang) preferences
(7, 3, 6, 1, 'APPROVED', 'Em quan tâm đến lĩnh vực mạng và muốn được thực tập tại VNPT', DATE_SUB(NOW(), INTERVAL 55 DAY), NOW(), NULL),
(8, 3, 7, 2, 'CANCELLED', 'Em thích công việc kiểm thử phần mềm', DATE_SUB(NOW(), INTERVAL 55 DAY), NOW(), NULL),
(9, 3, 8, 3, 'PENDING', 'Em cũng quan tâm đến DevOps', DATE_SUB(NOW(), INTERVAL 55 DAY), NOW(), NULL),

-- Student 4 (Vũ Quang Minh) preferences
(10, 4, 7, 1, 'APPROVED', 'Em mong muốn được thực tập tại MISA để học hỏi về quy trình kiểm thử chuyên nghiệp', DATE_SUB(NOW(), INTERVAL 57 DAY), NOW(), NULL),
(11, 4, 8, 2, 'CANCELLED', 'Em quan tâm đến DevOps và CI/CD', DATE_SUB(NOW(), INTERVAL 57 DAY), NOW(), NULL),
(12, 4, 10, 3, 'PENDING', 'Em cũng thích phân tích nghiệp vụ', DATE_SUB(NOW(), INTERVAL 57 DAY), NOW(), NULL),

-- Student 5 (Nguyễn Hoàng Sơn) preferences
(13, 5, 11, 1, 'APPROVED', 'Em mong muốn được thực tập về Cloud Computing tại Microsoft', DATE_SUB(NOW(), INTERVAL 56 DAY), NOW(), NULL),
(14, 5, 12, 2, 'CANCELLED', 'Em quan tâm đến Machine Learning', DATE_SUB(NOW(), INTERVAL 56 DAY), NOW(), NULL),
(15, 5, 1, 3, 'PENDING', 'Em cũng có kinh nghiệm với Java', DATE_SUB(NOW(), INTERVAL 56 DAY), NOW(), NULL);

-- External Internships for students who do internships at non-partner companies
INSERT INTO external_internships (id, student_id, period_id, confirmation_file_path, status, created_at, updated_at) VALUES
(1, 6, '2024.2', '/uploads/confirmations/20216345/confirmation_letter.pdf', 'APPROVED', DATE_SUB(NOW(), INTERVAL 65 DAY), NOW()),
(2, 7, '2024.2', '/uploads/confirmations/20216678/confirmation_letter.pdf', 'APPROVED', DATE_SUB(NOW(), INTERVAL 63 DAY), NOW()),
(3, 8, '2024.2', '/uploads/confirmations/20216901/confirmation_letter.pdf', 'APPROVED', DATE_SUB(NOW(), INTERVAL 62 DAY), NOW());

-- Internship Progress records for all students
INSERT INTO internship_progress (id, student_id, position_id, period_id, teacher_id, start_date, end_date, is_external, external_id, status, company_name, position_title, supervisor_name, supervisor_position, supervisor_email, supervisor_phone, teacher_confirmed, teacher_confirmed_at, created_at, updated_at, deleted_at) VALUES
-- Regular internships (students 1-5)
(1, 1, 1, '2024.2', 1, '2025-02-10', '2025-06-08', false, NULL, 'IN_PROGRESS', NULL, NULL, 'Nguyễn Văn Hải', 'Senior Java Developer', 'hai.nv@fpt.com.vn', '0987654321', true, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 55 DAY), NOW(), NULL),
(2, 2, 4, '2024.2', 2, '2025-02-10', '2025-06-08', false, NULL, 'IN_PROGRESS', NULL, NULL, 'Trần Minh Tuấn', 'Project Manager', 'tuan.tm@viettel.com.vn', '0987654322', true, DATE_SUB(NOW(), INTERVAL 50 DAY), DATE_SUB(NOW(), INTERVAL 54 DAY), NOW(), NULL),
(3, 3, 6, '2024.2', 3, '2025-02-10', '2025-06-08', false, NULL, 'IN_PROGRESS', NULL, NULL, 'Lê Văn Bình', 'Network Manager', 'binh.lv@vnpt.com.vn', '0987654323', true, DATE_SUB(NOW(), INTERVAL 49 DAY), DATE_SUB(NOW(), INTERVAL 53 DAY), NOW(), NULL),
(4, 4, 7, '2024.2', 1, '2025-02-10', '2025-06-08', false, NULL, 'IN_PROGRESS', NULL, NULL, 'Phạm Thị Hà', 'QA Manager', 'ha.pt@misa.com.vn', '0987654383', true, DATE_SUB(NOW(), INTERVAL 48 DAY), DATE_SUB(NOW(), INTERVAL 52 DAY), NOW(), NULL),
(5, 5, 11, '2024.2', 2, '2025-02-10', '2025-06-08', false, NULL, 'IN_PROGRESS', NULL, NULL, 'Nguyễn Văn Toàn', 'Cloud Engineer', 'toan.nv@microsoft.com', '0912345685', true, DATE_SUB(NOW(), INTERVAL 47 DAY), DATE_SUB(NOW(), INTERVAL 52 DAY), NOW(), NULL),

-- External internships (students 6-8)
(6, 6, NULL, '2024.2', 3, '2025-02-10', '2025-06-08', true, 1, 'IN_PROGRESS', 'NAB Vietnam', 'Frontend Developer', 'Đỗ Thị Hương', 'HR Director', 'huong.dt@nab.com.vn', '0987654324', true, DATE_SUB(NOW(), INTERVAL 48 DAY), DATE_SUB(NOW(), INTERVAL 53 DAY), NOW(), NULL),
(7, 7, NULL, '2024.2', 1, '2025-02-10', '2025-06-08', true, 2, 'IN_PROGRESS', 'Tinh Van Consulting', 'Backend Developer', 'Ngô Văn Minh', 'CTO', 'minh.nv@tinhvan.com', '0987654325', true, DATE_SUB(NOW(), INTERVAL 47 DAY), DATE_SUB(NOW(), INTERVAL 52 DAY), NOW(), NULL),
(8, 8, NULL, '2024.2', 2, '2025-02-10', '2025-06-08', true, 3, 'IN_PROGRESS', 'FPT Software HCM', 'Software Testing Engineer', 'Trần Thị Lan', 'Software Architect', 'lan.tt@fsoft.com.vn', '0987654326', true, DATE_SUB(NOW(), INTERVAL 46 DAY), DATE_SUB(NOW(), INTERVAL 51 DAY), NOW(), NULL);