-- Insert Student References
INSERT INTO student_references (id, student_id, student_code, full_name) VALUES
                                                                             (1, 1, '20216805', 'Nguyễn Tiến Dũng'),
                                                                             (2, 2, '20216123', 'Lê Thị Anh'),
                                                                             (3, 3, '20216456', 'Nguyễn Thị Trang'),
                                                                             (4, 4, '20216789', 'Vũ Quang Minh'),
                                                                             (5, 5, '20216012', 'Nguyễn Hoàng Sơn'),
                                                                             (6, 6, '20216345', 'Nguyễn Văn Hiền'),
                                                                             (7, 7, '20216678', 'Nguyễn Văn Thành'),
                                                                             (8, 8, '20216901', 'Nguyễn Thị Lam');

-- Insert Company References
INSERT INTO company_references (id, company_id, name) VALUES
                                                          (1, 1, 'FPT Software'),
                                                          (2, 2, 'Viettel'),
                                                          (3, 3, 'VNPT'),
                                                          (4, 4, 'MISA'),
                                                          (5, 5, 'CMC Global');

-- Insert Teacher References
INSERT INTO teacher_references (id, teacher_id, full_name) VALUES
                                                               (1, 1, 'TS. Vũ Thành Nam'),
                                                               (2, 2, 'TS. Nguyễn Trung Hoàng'),
                                                               (3, 3, 'PGS.TS. Lê Đình Hưng'),
                                                               (4, 4, 'ThS. Phạm Văn Cường');

-- Insert Internship Periods
INSERT INTO internship_periods (id, name, start_date, end_date, registration_start_date, registration_end_date, status, description, created_at, updated_at) VALUES
                                                                                                                                                                 (1, 'Kỳ thực tập 20241', '2025-06-01', '2025-08-01', '2025-05-01', '2025-05-20', 'ACTIVE', 'Kỳ thực tập học kỳ hè năm học 2024-2025', NOW(), NOW()),
                                                                                                                                                                 (2, 'Kỳ thực tập 20242', '2025-12-01', '2026-02-01', '2025-11-01', '2025-11-20', 'UPCOMING', 'Kỳ thực tập học kỳ đông năm học 2025-2026', NOW(), NOW());

-- Insert Internship Positions
INSERT INTO internship_positions (id, company_ref_id, period_id, title, description, requirements, benefits, available_slots, registered_count, work_type, due_date, created_at, updated_at) VALUES
                                                                                                                                                                                                 (1, 1, 1, 'Java Developer Intern', 'Thực tập sinh tham gia phát triển các ứng dụng web sử dụng Java Spring Boot và ReactJS', 'Sinh viên năm cuối ngành CNTT, KHMT; Có kiến thức về Java, Spring Boot; Biết sử dụng HTML, CSS, JavaScript là lợi thế', 'Môi trường làm việc chuyên nghiệp; Cơ hội được học hỏi từ các chuyên gia; Hỗ trợ ăn trưa và đi lại', 5, 3, 'FULL_TIME', '2025-05-20', NOW(), NOW()),
                                                                                                                                                                                                 (2, 1, 1, 'Frontend Developer Intern', 'Thực tập sinh tham gia phát triển giao diện người dùng cho các ứng dụng web', 'Sinh viên năm cuối ngành CNTT, KHMT; Có kiến thức về HTML, CSS, JavaScript; Biết sử dụng ReactJS hoặc VueJS là lợi thế', 'Môi trường làm việc chuyên nghiệp; Cơ hội được học hỏi từ các chuyên gia; Hỗ trợ ăn trưa và đi lại', 4, 2, 'FULL_TIME', '2025-05-20', NOW(), NOW()),
                                                                                                                                                                                                 (3, 2, 1, 'Software Engineer Intern', 'Thực tập sinh tham gia phát triển các ứng dụng di động và web', 'Sinh viên năm cuối ngành CNTT, KHMT; Có kiến thức về Java, Kotlin hoặc JavaScript; Biết sử dụng Android Studio là lợi thế', 'Môi trường làm việc năng động; Cơ hội được học hỏi từ các chuyên gia; Trợ cấp thực tập hấp dẫn', 6, 4, 'FULL_TIME', '2025-05-20', NOW(), NOW()),
                                                                                                                                                                                                 (4, 3, 1, 'Web Developer Intern', 'Thực tập sinh tham gia phát triển các ứng dụng web sử dụng PHP và Laravel', 'Sinh viên năm cuối ngành CNTT, KHMT; Có kiến thức về PHP, MySQL; Biết sử dụng Laravel là lợi thế', 'Môi trường làm việc thân thiện; Cơ hội được học hỏi từ các chuyên gia; Hỗ trợ ăn trưa và đi lại', 3, 1, 'FULL_TIME', '2025-05-20', NOW(), NOW()),
                                                                                                                                                                                                 (5, 4, 1, 'Backend Developer Intern', 'Thực tập sinh tham gia phát triển backend cho các ứng dụng web', 'Sinh viên năm cuối ngành CNTT, KHMT; Có kiến thức về C#, .NET, SQL Server; Biết sử dụng Entity Framework là lợi thế', 'Môi trường làm việc chuyên nghiệp; Cơ hội được học hỏi từ các chuyên gia; Hỗ trợ ăn trưa và đi lại', 4, 0, 'FULL_TIME', '2025-05-20', NOW(), NOW()),
                                                                                                                                                                                                 (6, 5, 1, 'Data Analyst Intern', 'Thực tập sinh tham gia phân tích dữ liệu và xây dựng các mô hình thống kê', 'Sinh viên năm cuối ngành CNTT, KHMT, Toán Tin; Có kiến thức về Python, R; Biết sử dụng các thư viện phân tích dữ liệu là lợi thế', 'Môi trường làm việc năng động; Cơ hội được học hỏi từ các chuyên gia; Trợ cấp thực tập hấp dẫn', 3, 0, 'FULL_TIME', '2025-05-20', NOW(), NOW());

-- Insert CV References
INSERT INTO cv_references (id, original_cv_id, student_ref_id, file_name, upload_date) VALUES
                                                                                           (1, 1, 1, 'CV_NguyenTienDung_2024.pdf', DATE_SUB(NOW(), INTERVAL 30 DAY)),
                                                                                           (2, 2, 1, 'CV_NguyenTienDung_2024_v2.pdf', DATE_SUB(NOW(), INTERVAL 15 DAY)),
                                                                                           (3, 3, 2, 'CV_LeThiAnh_2024.pdf', DATE_SUB(NOW(), INTERVAL 25 DAY)),
                                                                                           (4, 4, 3, 'CV_NguyenThiTrang_2024.pdf', DATE_SUB(NOW(), INTERVAL 20 DAY)),
                                                                                           (5, 5, 4, 'CV_VuQuangMinh_2024.pdf', DATE_SUB(NOW(), INTERVAL 22 DAY)),
                                                                                           (6, 6, 5, 'CV_NguyenHoangSon_2024.pdf', DATE_SUB(NOW(), INTERVAL 18 DAY)),
                                                                                           (7, 7, 6, 'CV_NguyenVanHien_2024.pdf', DATE_SUB(NOW(), INTERVAL 21 DAY)),
                                                                                           (8, 8, 7, 'CV_NguyenVanThanh_2024.pdf', DATE_SUB(NOW(), INTERVAL 19 DAY)),
                                                                                           (9, 9, 8, 'CV_NguyenThiLam_2024.pdf', DATE_SUB(NOW(), INTERVAL 23 DAY));

-- Insert Internship Applications
INSERT INTO internship_applications (id, student_ref_id, position_id, period_id, preference_order, status, applied_date, cv_ref_id, note, updated_at) VALUES
                                                                                                                                                          (1, 1, 1, 1, 1, 'PENDING', DATE_SUB(NOW(), INTERVAL 10 DAY), 2, 'Mong muốn học hỏi và phát triển kỹ năng lập trình Java', NOW()),
                                                                                                                                                          (2, 1, 3, 1, 2, 'PENDING', DATE_SUB(NOW(), INTERVAL 10 DAY), 2, 'Muốn tìm hiểu về phát triển ứng dụng di động', NOW()),
                                                                                                                                                          (3, 1, 4, 1, 3, 'PENDING', DATE_SUB(NOW(), INTERVAL 10 DAY), 2, 'Có kinh nghiệm làm việc với PHP', NOW()),
                                                                                                                                                          (4, 2, 3, 1, 1, 'APPROVED', DATE_SUB(NOW(), INTERVAL 12 DAY), 3, 'Có kinh nghiệm phát triển ứng dụng Android', NOW()),
                                                                                                                                                          (5, 2, 1, 1, 2, 'REJECTED', DATE_SUB(NOW(), INTERVAL 12 DAY), 3, 'Đã có kinh nghiệm với Spring Boot', NOW()),
                                                                                                                                                          (6, 2, 5, 1, 3, 'PENDING', DATE_SUB(NOW(), INTERVAL 12 DAY), 3, 'Có kinh nghiệm với .NET', NOW()),
                                                                                                                                                          (7, 3, 2, 1, 1, 'APPROVED', DATE_SUB(NOW(), INTERVAL 11 DAY), 4, 'Có kinh nghiệm với ReactJS', NOW()),
                                                                                                                                                          (8, 4, 6, 1, 1, 'PENDING', DATE_SUB(NOW(), INTERVAL 9 DAY), 5, 'Có kinh nghiệm với Python và phân tích dữ liệu', NOW()),
                                                                                                                                                          (9, 5, 1, 1, 1, 'PENDING', DATE_SUB(NOW(), INTERVAL 8 DAY), 6, 'Muốn phát triển kỹ năng lập trình Java', NOW()),
                                                                                                                                                          (10, 6, 3, 1, 1, 'APPROVED', DATE_SUB(NOW(), INTERVAL 7 DAY), 7, 'Có kinh nghiệm phát triển ứng dụng Android', NOW()),
                                                                                                                                                          (11, 7, 4, 1, 1, 'APPROVED', DATE_SUB(NOW(), INTERVAL 6 DAY), 8, 'Có kinh nghiệm với Laravel', NOW());

-- Insert External Internships
INSERT INTO external_internships (id, student_ref_id, period_id, company_name, company_address, position, supervisor_name, supervisor_position, supervisor_email, supervisor_phone, start_date, end_date, work_schedule, confirmation_file_path, status, approval_date, created_at, updated_at) VALUES
    (1, 8, 1, 'Teko Vietnam', 'Số 85 Vũ Trọng Phụng, Thanh Xuân, Hà Nội', 'Frontend Developer Intern', 'Nguyễn Văn An', 'Technical Lead', 'an.nv@teko.vn', '0912345698', '2025-06-01', '2025-08-01', 'Thứ 2 - Thứ 6, 8:30 - 17:30', '/uploads/confirmations/20216901_confirmation.pdf', 'APPROVED', DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), NOW());

-- Insert Internship Progress
INSERT INTO internship_progress (id, student_ref_id, position_id, period_id, teacher_ref_id, start_date, end_date, is_external, external_id, status, supervisor_name, supervisor_position, supervisor_email, supervisor_phone, teacher_confirmed, teacher_confirmed_at, created_at, updated_at) VALUES
                                                                                                                                                                                                                                                                                                    (1, 2, 3, 1, 1, '2025-06-01', '2025-08-01', false, null, 'IN_PROGRESS', 'Trần Văn Hùng', 'Senior Developer', 'hung.tv@viettel.com.vn', '0912345680', true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NOW()),
                                                                                                                                                                                                                                                                                                    (2, 3, 2, 1, 2, '2025-06-01', '2025-08-01', false, null, 'IN_PROGRESS', 'Nguyễn Thị Hương', 'Frontend Team Lead', 'huong.nt@fpt.com.vn', '0912345678', true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NOW()),
                                                                                                                                                                                                                                                                                                    (3, 6, 3, 1, 3, '2025-06-01', '2025-08-01', false, null, 'IN_PROGRESS', 'Trần Văn Hùng', 'Senior Developer', 'hung.tv@viettel.com.vn', '0912345680', true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NOW()),
                                                                                                                                                                                                                                                                                                    (4, 7, 4, 1, 4, '2025-06-01', '2025-08-01', false, null, 'IN_PROGRESS', 'Nguyễn Văn Thắng', 'Web Development Lead', 'thang.nv@vnpt.com.vn', '0912345682', true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NOW()),
                                                                                                                                                                                                                                                                                                    (5, 8, null, 1, 1, '2025-06-01', '2025-08-01', true, 1, 'IN_PROGRESS', 'Nguyễn Văn An', 'Technical Lead', 'an.nv@teko.vn', '0912345698', true, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW(), NOW());