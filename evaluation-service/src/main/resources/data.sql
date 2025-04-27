INSERT INTO evaluation_criteria (id, name, description, created_at, updated_at) VALUES
(1, 'Kiến thức chuyên môn', 'Đánh giá kiến thức chuyên môn và khả năng áp dụng lý thuyết vào thực tiễn', NOW(), NOW()),
(2, 'Kỹ năng lập trình', 'Đánh giá khả năng viết mã nguồn, giải quyết vấn đề và tư duy thuật toán', NOW(), NOW()),
(3, 'Khả năng làm việc nhóm', 'Đánh giá mức độ hợp tác, giao tiếp và làm việc với các thành viên khác trong nhóm', NOW(), NOW()),
(4, 'Thái độ làm việc', 'Đánh giá tính cẩn thận, trách nhiệm và tính chuyên nghiệp trong công việc', NOW(), NOW()),
(5, 'Khả năng tự học', 'Đánh giá khả năng tự nghiên cứu, học hỏi công nghệ mới', NOW(), NOW()),
(6, 'Khả năng quản lý thời gian', 'Đánh giá khả năng sắp xếp công việc, đảm bảo tiến độ và hoàn thành đúng hạn', NOW(), NOW());

INSERT INTO company_evaluations (id, progress_id, evaluation_date, score, comments) VALUES
(1, 1, DATE_SUB(NOW(), INTERVAL 2 DAY), 8.5, 'Sinh viên có năng lực tốt, nhanh nhẹn và có khả năng học hỏi nhanh. Đã hoàn thành tốt các nhiệm vụ được giao và đóng góp tích cực vào dự án.'),
(2, 2, DATE_SUB(NOW(), INTERVAL 3 DAY), 8.0, 'Sinh viên làm việc chăm chỉ, có kiến thức nền tảng vững và có khả năng làm việc nhóm tốt. Cần cải thiện thêm về kỹ năng giao tiếp.'),
(3, 3, DATE_SUB(NOW(), INTERVAL 4 DAY), 7.5, 'Sinh viên có thái độ làm việc tốt, chịu khó học hỏi. Cần phát triển thêm về khả năng giải quyết vấn đề và tư duy phản biện.');

INSERT INTO company_evaluation_details (id, evaluation_id, criteria_id, comments) VALUES
(1, 1, 1, 'Sinh viên có kiến thức cơ bản về Java Spring vững, hiểu rõ về RESTful API và JPA.'),
(2, 1, 2, 'Có khả năng viết mã nguồn tốt, phong cách code rõ ràng, dễ đọc.'),
(3, 1, 3, 'Hòa đồng, biết lắng nghe và đóng góp ý kiến cho nhóm.'),
(4, 1, 4, 'Nghiêm túc, có trách nhiệm với công việc được giao.'),
(5, 1, 5, 'Tự tìm hiểu tốt về các công nghệ mới khi được yêu cầu.'),
(6, 1, 6, 'Quản lý thời gian tốt, hoàn thành công việc đúng hạn.'),
(7, 2, 1, 'Có kiến thức khá tốt về phát triển phần mềm, hiểu về quy trình phát triển.'),
(8, 2, 2, 'Kỹ năng lập trình tốt, có khả năng debug và giải quyết vấn đề.'),
(9, 2, 3, 'Làm việc nhóm tốt, chủ động đề xuất giải pháp.'),
(10, 2, 4, 'Có tinh thần trách nhiệm cao, luôn hoàn thành công việc được giao.'),
(11, 2, 5, 'Khả năng tự học tốt, chủ động tìm hiểu các công nghệ mới.'),
(12, 2, 6, 'Đôi khi quản lý thời gian chưa tốt, cần cải thiện.'),
(13, 3, 1, 'Kiến thức về mạng máy tính khá tốt, hiểu biết về TCP/IP.'),
(14, 3, 2, 'Kỹ năng thao tác với thiết bị mạng tốt, hiểu về cấu hình cơ bản.'),
(15, 3, 3, 'Làm việc nhóm còn hạn chế, cần cải thiện về giao tiếp.'),
(16, 3, 4, 'Thái độ làm việc tốt, cẩn thận và tỉ mỉ.'),
(17, 3, 5, 'Khả năng tự học khá, nhưng cần chủ động hơn.'),
(18, 3, 6, 'Quản lý thời gian chưa tốt, đôi khi không hoàn thành đúng hạn.');

INSERT INTO internship_reports (id, progress_id, title, content, file_path, submission_date) VALUES
(1, 1, 'Báo cáo thực tập tại FPT Software - Tuần 1', 'Trong tuần đầu tiên, em đã làm quen với môi trường làm việc tại FPT Software, được giới thiệu về quy trình phát triển phần mềm của công ty và bắt đầu tìm hiểu về dự án sẽ tham gia.', '/uploads/reports/20216805/report_week1.pdf', DATE_SUB(NOW(), INTERVAL 20 DAY)),
(2, 1, 'Báo cáo thực tập tại FPT Software - Tuần 2', 'Trong tuần thứ hai, em đã được phân công nhiệm vụ đầu tiên là phát triển một API đơn giản. Em đã học được cách sử dụng Spring Boot để tạo RESTful API và tích hợp với cơ sở dữ liệu MySQL.', '/uploads/reports/20216805/report_week2.pdf', DATE_SUB(NOW(), INTERVAL 13 DAY)),
(3, 2, 'Báo cáo thực tập tại Viettel - Tuần 1', 'Trong tuần đầu tiên tại Viettel, em đã được giới thiệu về văn hóa công ty và quy trình làm việc. Em cũng đã được tham gia vào các buổi họp nhóm để hiểu rõ hơn về dự án.', '/uploads/reports/20216123/report_week1.pdf', DATE_SUB(NOW(), INTERVAL 19 DAY)),
(4, 3, 'Báo cáo thực tập tại VNPT - Tuần 1', 'Trong tuần đầu tiên tại VNPT, em đã được hướng dẫn về hệ thống mạng của công ty và các công nghệ đang được sử dụng. Em cũng đã được tham gia vào quá trình kiểm tra và bảo trì thiết bị mạng.', '/uploads/reports/20216456/report_week1.pdf', DATE_SUB(NOW(), INTERVAL 18 DAY));