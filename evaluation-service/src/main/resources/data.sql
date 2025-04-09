-- Insert Progress References
INSERT INTO progress_references (id, original_progress_id, student_name, period_name, position_title, company_name, created_at) VALUES
                                                                                                                                    (1, 1, 'Lê Thị Anh', 'Kỳ thực tập 20241', 'Software Engineer Intern', 'Viettel', NOW()),
                                                                                                                                    (2, 2, 'Nguyễn Thị Trang', 'Kỳ thực tập 20241', 'Frontend Developer Intern', 'FPT Software', NOW()),
                                                                                                                                    (3, 3, 'Nguyễn Văn Hiền', 'Kỳ thực tập 20241', 'Software Engineer Intern', 'Viettel', NOW()),
                                                                                                                                    (4, 4, 'Nguyễn Văn Thành', 'Kỳ thực tập 20241', 'Web Developer Intern', 'VNPT', NOW()),
                                                                                                                                    (5, 5, 'Nguyễn Thị Lam', 'Kỳ thực tập 20241', 'Frontend Developer Intern', 'Teko Vietnam', NOW());

-- Insert Internship Reports
INSERT INTO internship_reports (id, progress_ref_id, title, content, file_path, submission_date) VALUES
                                                                                                     (1, 1, 'Báo cáo thực tập tuần 1 - Lê Thị Anh', 'Trong tuần đầu tiên thực tập tại Viettel, tôi đã được giới thiệu về môi trường làm việc, các quy định và văn hóa công ty. Tôi đã được tham gia vào team phát triển ứng dụng di động và được hướng dẫn về quy trình phát triển phần mềm tại công ty. Ngoài ra, tôi cũng đã được giao nhiệm vụ tìm hiểu về framework React Native và thực hiện một số task nhỏ.', '/uploads/reports/LeThiAnh_Week1_Report.pdf', DATE_SUB(NOW(), INTERVAL 25 DAY)),
                                                                                                     (2, 1, 'Báo cáo thực tập tuần 2 - Lê Thị Anh', 'Trong tuần thứ hai, tôi đã được giao nhiệm vụ phát triển một tính năng nhỏ trong ứng dụng di động của công ty. Tôi đã học được cách sử dụng Git flow và quy trình code review tại công ty. Ngoài ra, tôi cũng đã tham gia các buổi daily standup meeting và sprint planning.', '/uploads/reports/LeThiAnh_Week2_Report.pdf', DATE_SUB(NOW(), INTERVAL 18 DAY)),
                                                                                                     (3, 2, 'Báo cáo thực tập tuần 1 - Nguyễn Thị Trang', 'Trong tuần đầu tiên thực tập tại FPT Software, tôi đã được giới thiệu về công ty, các dự án hiện tại và team mà tôi sẽ làm việc. Tôi đã được hướng dẫn về quy trình làm việc, các công cụ và công nghệ mà team đang sử dụng. Tôi đã được giao nhiệm vụ nghiên cứu về ReactJS và các best practices.', '/uploads/reports/NguyenThiTrang_Week1_Report.pdf', DATE_SUB(NOW(), INTERVAL 25 DAY)),
                                                                                                     (4, 2, 'Báo cáo thực tập tuần 2 - Nguyễn Thị Trang', 'Trong tuần thứ hai, tôi đã được giao nhiệm vụ phát triển một component UI cho dự án. Tôi đã học được cách sử dụng styled-components và Redux để quản lý state trong ứng dụng. Tôi cũng đã tham gia vào các buổi code review và học hỏi được nhiều kinh nghiệm từ các đồng nghiệp.', '/uploads/reports/NguyenThiTrang_Week2_Report.pdf', DATE_SUB(NOW(), INTERVAL 18 DAY)),
                                                                                                     (5, 3, 'Báo cáo thực tập tuần 1 - Nguyễn Văn Hiền', 'Trong tuần đầu tiên thực tập tại Viettel, tôi đã được giới thiệu về môi trường làm việc và các quy định của công ty. Tôi đã được phân công vào team phát triển backend và được hướng dẫn về quy trình phát triển phần mềm tại công ty. Tôi đã được giao nhiệm vụ tìm hiểu về Spring Boot và các microservices đang được sử dụng.', '/uploads/reports/NguyenVanHien_Week1_Report.pdf', DATE_SUB(NOW(), INTERVAL 25 DAY));

-- Insert Evaluation Criteria
INSERT INTO evaluation_criteria (id, name, description, created_at, updated_at) VALUES
                                                                                    (1, 'Kỹ năng chuyên môn', 'Đánh giá về kiến thức và kỹ năng chuyên môn của sinh viên', NOW(), NOW()),
                                                                                    (2, 'Khả năng học hỏi', 'Đánh giá về khả năng tiếp thu kiến thức mới của sinh viên', NOW(), NOW()),
                                                                                    (3, 'Khả năng làm việc nhóm', 'Đánh giá về khả năng phối hợp và làm việc với đồng nghiệp', NOW(), NOW()),
                                                                                    (4, 'Thái độ làm việc', 'Đánh giá về thái độ, tinh thần làm việc của sinh viên', NOW(), NOW()),
                                                                                    (5, 'Khả năng giải quyết vấn đề', 'Đánh giá về khả năng phân tích và giải quyết vấn đề của sinh viên', NOW(), NOW()),
                                                                                    (6, 'Kỹ năng giao tiếp', 'Đánh giá về kỹ năng giao tiếp, trình bày của sinh viên', NOW(), NOW());

-- Insert Company Evaluations
INSERT INTO company_evaluations (id, progress_ref_id, evaluation_date, overall_score, comments) VALUES
                                                                                                    (1, 1, DATE_SUB(NOW(), INTERVAL 5 DAY), 8.5, 'Lê Thị Anh là một thực tập sinh có năng lực và tinh thần học hỏi tốt. Em đã hoàn thành tốt các nhiệm vụ được giao và luôn sẵn sàng hỗ trợ đồng nghiệp. Em cần cải thiện thêm về kỹ năng debug và tối ưu code.'),
                                                                                                    (2, 2, DATE_SUB(NOW(), INTERVAL 5 DAY), 9.0, 'Nguyễn Thị Trang là một thực tập sinh xuất sắc. Em có kiến thức tốt về frontend và học hỏi nhanh. Các component do em phát triển đều đáp ứng yêu cầu về UI/UX và performance. Em giao tiếp tốt và luôn chủ động trong công việc.');

-- Insert Company Evaluation Details
INSERT INTO company_evaluation_details (id, evaluation_id, criteria_id, score, comments) VALUES
                                                                                             (1, 1, 1, 8.0, 'Có kiến thức tốt về Android development và Java'),
                                                                                             (2, 1, 2, 9.0, 'Học hỏi nhanh và luôn chủ động tự tìm hiểu'),
                                                                                             (3, 1, 3, 8.5, 'Hòa đồng và phối hợp tốt với đồng nghiệp'),
                                                                                             (4, 1, 4, 9.0, 'Nghiêm túc, có trách nhiệm trong công việc'),
                                                                                             (5, 1, 5, 8.0, 'Có khả năng phân tích và giải quyết vấn đề tốt'),
                                                                                             (6, 1, 6, 8.5, 'Giao tiếp tốt, trình bày ý tưởng rõ ràng'),
                                                                                             (7, 2, 1, 9.0, 'Có kiến thức tốt về ReactJS và các thư viện UI'),
                                                                                             (8, 2, 2, 9.5, 'Rất nhanh nhạy trong việc tiếp thu kiến thức mới'),
                                                                                             (9, 2, 3, 8.5, 'Làm việc nhóm tốt, luôn sẵn sàng giúp đỡ đồng nghiệp'),
                                                                                             (10, 2, 4, 9.0, 'Nghiêm túc và chăm chỉ'),
                                                                                             (11, 2, 5, 9.0, 'Giải quyết vấn đề hiệu quả, có tư duy logic tốt'),
                                                                                             (12, 2, 6, 9.0, 'Giao tiếp tốt, trình bày ý tưởng rõ ràng và logic');