-- Insert Progress References
INSERT INTO progress_references (id, original_progress_id, student_name, period_name, position_title, company_name, created_at) VALUES
                                                                                                                                    (1, 1, 'Lê Thị Anh', 'Kỳ thực tập 20241', 'Software Engineer Intern', 'Viettel', NOW()),
                                                                                                                                    (2, 2, 'Nguyễn Thị Trang', 'Kỳ thực tập 20241', 'Frontend Developer Intern', 'FPT Software', NOW()),
                                                                                                                                    (3, 3, 'Nguyễn Văn Hiền', 'Kỳ thực tập 20241', 'Software Engineer Intern', 'Viettel', NOW()),
                                                                                                                                    (4, 4, 'Nguyễn Văn Thành', 'Kỳ thực tập 20241', 'Web Developer Intern', 'VNPT', NOW()),
                                                                                                                                    (5, 5, 'Nguyễn Thị Lam', 'Kỳ thực tập 20241', 'Frontend Developer Intern', 'Teko Vietnam', NOW());

-- Insert Internship Reports
INSERT INTO internship_reports (id, progress_ref_id, title, content, file_path, submission_date) VALUES
                                                                                                     (1, 1, 'Báo cáo thực tập - Lê Thị Anh', 'Trong hai tháng thực tập tại Viettel, tôi đã được tham gia vào dự án phát triển ứng dụng di động cho khách hàng doanh nghiệp. Dự án sử dụng Android Studio với ngôn ngữ Java và Kotlin. Tôi đã học được quy trình phát triển phần mềm chuyên nghiệp từ việc lấy yêu cầu, thiết kế, phát triển, kiểm thử đến triển khai. Tôi đã phát triển một số tính năng như quản lý thông tin người dùng, thống kê báo cáo và tích hợp thanh toán. Qua quá trình thực tập, tôi đã cải thiện kỹ năng lập trình, làm việc nhóm và giao tiếp chuyên nghiệp. Tôi cũng học hỏi được cách làm việc trong môi trường doanh nghiệp lớn với các quy trình và tiêu chuẩn chất lượng cao.', '/uploads/reports/LeThiAnh_Final_Report.pdf', DATE_SUB(NOW(), INTERVAL 5 DAY)),
                                                                                                     (2, 2, 'Báo cáo thực tập - Nguyễn Thị Trang', 'Trong thời gian thực tập tại FPT Software, tôi đã được tham gia vào dự án phát triển ứng dụng web cho một khách hàng quốc tế trong lĩnh vực tài chính. Dự án sử dụng ReactJS, Redux và các thư viện UI hiện đại. Tôi được giao nhiệm vụ phát triển các component UI và tích hợp API. Tôi đã học được cách làm việc trong một dự án thực tế với các yêu cầu nghiêm ngặt về bảo mật và hiệu năng. Quá trình thực tập đã giúp tôi cải thiện đáng kể kỹ năng lập trình frontend, hiểu sâu hơn về các best practices trong phát triển web, và học hỏi cách làm việc hiệu quả trong một team đa quốc gia. Tôi cũng đã có cơ hội tham gia các buổi đào tạo nội bộ về các công nghệ mới và soft skills.', '/uploads/reports/NguyenThiTrang_Final_Report.pdf', DATE_SUB(NOW(), INTERVAL 5 DAY));

-- Insert Evaluation Criteria
INSERT INTO evaluation_criteria (id, name, description, created_at, updated_at) VALUES
                                                                                    (1, 'Về Chuyên môn, nghiệp vụ:', 'Đánh giá về Chuyên môn, nghiệp vụ của sinh viên', NOW(), NOW()),
                                                                                    (2, 'Về Kỹ năng (Testing/ Nghiên cứu/ Viết báo cáo/ Thuyết trình, ...):', 'Đánh giá về Kỹ năng (Testing/ Nghiên cứu/ Viết báo cáo/ Thuyết trình, ...) của sinh viên', NOW(), NOW()),
                                                                                    (3, 'Về Ứng xử doanh nghiệp', 'Đánh giá về Ứng xử doanh nghiệp của sinh viên', NOW(), NOW());

-- Insert Company Evaluations
INSERT INTO company_evaluations (id, progress_ref_id, evaluation_date, score, comments) VALUES
                                                                                                    (1, 1, DATE_SUB(NOW(), INTERVAL 3 DAY), 8.5, 'Lê Thị Anh là một thực tập sinh có năng lực và tinh thần học hỏi tốt. Em đã hoàn thành tốt các nhiệm vụ được giao trong dự án phát triển ứng dụng di động và thể hiện khả năng làm việc nhóm tốt. Em có kiến thức vững về Java và Kotlin, nhanh chóng tiếp thu các kỹ thuật mới trong phát triển ứng dụng di động. Trong quá trình thực tập, em đã đóng góp vào các tính năng quản lý người dùng và tích hợp thanh toán. Kỹ năng debug và phân tích vấn đề của em cần được cải thiện thêm, nhưng nhìn chung em đã thể hiện tiềm năng tốt và đạt được sự tiến bộ đáng kể trong thời gian thực tập.'),
                                                                                                    (2, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), 9.0, 'Nguyễn Thị Trang là một thực tập sinh xuất sắc. Em có kiến thức tốt về ReactJS và các thư viện UI hiện đại. Em nhanh chóng hòa nhập với team và làm việc hiệu quả trong môi trường đa quốc gia. Các component do em phát triển đều đáp ứng yêu cầu về UI/UX và performance. Em chủ động trong việc tìm hiểu, học hỏi và đề xuất giải pháp cải thiện. Kỹ năng giao tiếp và làm việc nhóm của em rất tốt. Em hoàn thành tất cả các nhiệm vụ được giao đúng hạn và chất lượng cao. Chúng tôi đánh giá cao tinh thần làm việc của em và sẵn sàng xem xét tuyển dụng chính thức sau khi em tốt nghiệp.');

-- Insert Company Evaluation Details
INSERT INTO company_evaluation_details (id, evaluation_id, criteria_id, comments) VALUES
                                                                                             (1, 1, 1, 'Kiến thức tốt về Android development sử dụng Java và Kotlin. Có khả năng áp dụng các mẫu thiết kế (design patterns) vào các vấn đề thực tế. Hiểu và áp dụng được nguyên tắc clean code vào dự án.'),
                                                                                             (2, 1, 2, 'Khả năng học hỏi nhanh, chủ động tìm hiểu công nghệ mới và áp dụng vào công việc. Tích cực tham gia các buổi chia sẻ kiến thức nội bộ và đặt câu hỏi sâu sắc.'),
                                                                                             (3, 1, 3, 'Làm việc tốt với các thành viên trong team, tham gia tích cực vào các cuộc họp và đóng góp ý kiến xây dựng. Sẵn sàng hỗ trợ đồng nghiệp khi cần thiết.'),
                                                                                             (4, 2, 1, 'Kiến thức vững về ReactJS, Redux và các công nghệ frontend hiện đại. Có khả năng tối ưu hiệu năng của ứng dụng web và áp dụng các best practices trong phát triển frontend.'),
                                                                                             (5, 2, 2, 'Khả năng học hỏi nhanh, tiếp thu nhanh các công nghệ và thư viện mới. Chủ động nghiên cứu và đề xuất giải pháp cải tiến cho dự án.'),
                                                                                             (6, 2, 3, 'Làm việc nhóm xuất sắc, phối hợp tốt với các thành viên khác trong team đa quốc gia. Chủ động chia sẻ kiến thức và hỗ trợ đồng nghiệp.');