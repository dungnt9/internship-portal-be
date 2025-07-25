-- Updated evaluation criteria with 3 main criteria
INSERT INTO evaluation_criteria (id, name, description, created_at, updated_at) VALUES
(1, 'Kiến thức chuyên môn', 'Đánh giá kiến thức chuyên môn, kỹ năng lập trình, và khả năng áp dụng lý thuyết vào thực tiễn. Bao gồm cả khả năng nắm bắt và sử dụng công nghệ, khả năng giải quyết vấn đề, và tư duy logic.', NOW(), NOW()),
(2, 'Kỹ năng mềm', 'Đánh giá khả năng giao tiếp, làm việc nhóm, quản lý thời gian, và tính chuyên nghiệp. Bao gồm cả thái độ làm việc, tính chủ động, sáng tạo và khả năng thích ứng với môi trường làm việc.', NOW(), NOW()),
(3, 'Kết quả công việc', 'Đánh giá chất lượng và số lượng công việc, khả năng hoàn thành nhiệm vụ đúng thời hạn, và đóng góp cho dự án. Bao gồm cả sự tiến bộ trong quá trình thực tập và khả năng học hỏi, phát triển.', NOW(), NOW());

-- Company evaluations for students doing internships
INSERT INTO company_evaluations (id, progress_id, evaluation_date, score, comments, created_at, updated_at) VALUES
-- Regular internships (students 1-5)
(1, 1, DATE_SUB(NOW(), INTERVAL 15 DAY), 8.5, 'Nguyễn Tiến Dũng có năng lực tốt, nhanh nhẹn và có khả năng học hỏi nhanh. Đã hoàn thành tốt các nhiệm vụ được giao và đóng góp tích cực vào dự án phát triển ứng dụng web. Có kỹ năng lập trình Java Spring Boot tốt và hiểu biết về kiến trúc MVC.', NOW(), NOW()),
(2, 2, DATE_SUB(NOW(), INTERVAL 14 DAY), 8.0, 'Nguyễn Tuấn Dũng làm việc chăm chỉ, có kiến thức nền tảng vững và khả năng làm việc nhóm tốt. Cần cải thiện thêm về kỹ năng giao tiếp và quản lý thời gian. Đã hoàn thành được 80% các nhiệm vụ được giao và có đóng góp tích cực vào dự án.', NOW(), NOW()),
(3, 3, DATE_SUB(NOW(), INTERVAL 13 DAY), 7.5, 'Nguyễn Thị Trang có thái độ làm việc tốt, chịu khó học hỏi. Cần phát triển thêm về khả năng giải quyết vấn đề và tư duy phản biện. Đã hoàn thành các nhiệm vụ cơ bản và có tiến bộ trong quá trình thực tập.', NOW(), NOW()),
(4, 4, DATE_SUB(NOW(), INTERVAL 12 DAY), NULL, NULL, NOW(), NOW()),
(5, 5, DATE_SUB(NOW(), INTERVAL 11 DAY), NULL, NULL, NOW(), NOW());

-- Company evaluation details
INSERT INTO company_evaluation_details (id, evaluation_id, criteria_id, comments, created_at, updated_at) VALUES
-- Student 1 (Nguyễn Tiến Dũng)
(1, 1, 1, 'Có kiến thức tốt về Java Spring Boot, hiểu rõ về RESTful API và JPA. Có khả năng viết mã nguồn sạch, rõ ràng và dễ bảo trì. Hiểu biết tốt về cơ sở dữ liệu và ORM.', NOW(), NOW()),
(2, 1, 2, 'Hòa đồng, biết lắng nghe và đóng góp ý kiến cho nhóm. Giao tiếp tốt và có khả năng thuyết trình ý tưởng rõ ràng. Quản lý thời gian tốt và có tinh thần trách nhiệm cao.', NOW(), NOW()),
(3, 1, 3, 'Hoàn thành tốt các nhiệm vụ được giao, đảm bảo chất lượng và tiến độ. Đóng góp nhiều ý tưởng hữu ích cho dự án và chủ động đề xuất các giải pháp cải thiện.', NOW(), NOW()),

-- Student 2 (Nguyễn Tuấn Dũng)
(4, 2, 1, 'Có kiến thức khá tốt về phát triển phần mềm, hiểu về quy trình phát triển. Kỹ năng lập trình tốt, có khả năng debug và giải quyết vấn đề. Nắm vững kiến thức về C++ và Java.', NOW(), NOW()),
(5, 2, 2, 'Làm việc nhóm tốt, chủ động đề xuất giải pháp. Có tinh thần trách nhiệm cao, luôn hoàn thành công việc được giao. Cần cải thiện kỹ năng giao tiếp và thuyết trình ý tưởng.', NOW(), NOW()),
(6, 2, 3, 'Hoàn thành được 80% các nhiệm vụ được giao. Chất lượng công việc tốt nhưng đôi khi chậm tiến độ. Có tiến bộ đáng kể trong quá trình thực tập.', NOW(), NOW()),
-- Student 3 (Nguyễn Thị Trang)
(7, 3, 1, 'Kiến thức về mạng máy tính khá tốt, hiểu biết về TCP/IP. Kỹ năng thao tác với thiết bị mạng tốt, hiểu về cấu hình cơ bản. Cần phát triển thêm về tư duy phân tích và giải quyết vấn đề.', NOW(), NOW()),
(8, 3, 2, 'Thái độ làm việc tốt, cẩn thận và tỉ mỉ. Khả năng làm việc nhóm còn hạn chế, cần cải thiện về giao tiếp. Quản lý thời gian chưa tốt, đôi khi không hoàn thành đúng hạn.', NOW(), NOW()),
(9, 3, 3, 'Hoàn thành các nhiệm vụ cơ bản được giao. Chất lượng công việc đạt yêu cầu nhưng chưa có nhiều đột phá. Có tiến bộ trong quá trình thực tập và có tinh thần học hỏi tốt.', NOW(), NOW()),

-- Student 4 (Vũ Quang Minh)
(10, 4, 1, NULL, NOW(), NOW()),
(11, 4, 2, NULL, NOW(), NOW()),
(12, 4, 3, NULL, NOW(), NOW()),

-- Student 5 (Nguyễn Hoàng Sơn)
(13, 5, 1, NULL, NOW(), NOW()),
(14, 5, 2, NULL, NOW(), NOW()),
(15, 5, 3, NULL, NOW(), NOW());


-- Internship Reports (1 comprehensive report per student)
INSERT INTO internship_reports (id, progress_id, title, content, file_path, submission_date, created_at, updated_at) VALUES
-- Regular internships (students 1-5)
(1, 1, 'Báo cáo thực tập tại FPT Software - Phát triển ứng dụng web với Java Spring Boot', 'Báo cáo chi tiết về quá trình thực tập tại FPT Software, bao gồm các kiến thức và kỹ năng đã học được, các công việc đã thực hiện, và đánh giá về kinh nghiệm thực tập. Trọng tâm của báo cáo là việc phát triển một ứng dụng web sử dụng Java Spring Boot, Spring Data JPA, và MySQL. Ứng dụng này là một hệ thống quản lý nhân sự với các chức năng cơ bản như thêm, sửa, xóa, và tìm kiếm nhân viên.\n\nBáo cáo cũng mô tả chi tiết về quy trình phát triển phần mềm tại FPT Software, các công cụ và công nghệ được sử dụng, và kinh nghiệm làm việc trong môi trường doanh nghiệp.', '/uploads/reports/20216805/2024.2/Report_20216805_2024.2_20250602_220750.pdf', DATE_SUB(NOW(), INTERVAL 5 DAY), NOW(), NOW()),

(2, 2, 'Báo cáo thực tập tại Viettel - Phát triển phần mềm trong lĩnh vực viễn thông', 'Báo cáo về quá trình thực tập tại Viettel, tập trung vào việc phát triển phần mềm trong lĩnh vực viễn thông. Báo cáo mô tả chi tiết về các dự án đã tham gia, các công nghệ và công cụ được sử dụng, và các kiến thức và kỹ năng đã học được.\n\nPhần chính của báo cáo tập trung vào việc phát triển một module quản lý thuê bao di động, bao gồm các chức năng như đăng ký thuê bao mới, cập nhật thông tin thuê bao, và quản lý dịch vụ. Báo cáo cũng mô tả về các thách thức đã gặp phải trong quá trình phát triển và các giải pháp đã áp dụng.', '/uploads/reports/20216123/2024.2/Report_20216123_2024.2_20250602_220823.pdf', DATE_SUB(NOW(), INTERVAL 6 DAY), NOW(), NOW()),

(3, 3, 'Báo cáo thực tập tại VNPT - Vận hành và phát triển hệ thống mạng', 'Báo cáo chi tiết về quá trình thực tập tại VNPT, tập trung vào lĩnh vực vận hành và phát triển hệ thống mạng. Báo cáo mô tả chi tiết về cấu trúc hệ thống mạng của VNPT, các công nghệ và thiết bị được sử dụng, và các quy trình vận hành và bảo trì.\n\nPhần chính của báo cáo tập trung vào việc thiết kế và triển khai một hệ thống giám sát mạng sử dụng các công cụ như Nagios và Grafana. Báo cáo cũng mô tả về các vấn đề và sự cố đã gặp phải trong quá trình vận hành và các giải pháp đã áp dụng.', '/uploads/reports/20216456/2024.2/Report_20216456_2024.2_20250602_220851.pdf', DATE_SUB(NOW(), INTERVAL 7 DAY), NOW(), NOW()),

(4, 4, '', NULL, NULL, NULL, NOW(), NOW()),
(5, 5, '', NULL, NULL, NULL, NOW(), NOW()),

-- External internships (students 6-8)
(6, 6, 'Báo cáo thực tập tại NAB - Phát triển ứng dụng web với React và Node.js', 'Báo cáo chi tiết về quá trình thực tập tại Ngân hàng NAB, tập trung vào lĩnh vực phát triển ứng dụng web. Báo cáo mô tả chi tiết về dự án đã tham gia, các công nghệ và công cụ được sử dụng, và các kiến thức và kỹ năng đã học được.\n\nPhần chính của báo cáo tập trung vào việc phát triển một ứng dụng web ngân hàng sử dụng React cho frontend và Node.js cho backend. Ứng dụng này cung cấp các chức năng như xem thông tin tài khoản, chuyển khoản, và quản lý các giao dịch. Báo cáo cũng mô tả về các thách thức đã gặp phải trong quá trình phát triển và các giải pháp đã áp dụng.', '/uploads/reports/20216345/2024.2/Report_20216345_2024.2_20250602_221040.pdf', DATE_SUB(NOW(), INTERVAL 10 DAY), NOW(), NOW()),

(7, 7, 'Báo cáo thực tập tại Công ty Tinh Vân - Phát triển phần mềm quản lý dự án', 'Báo cáo về quá trình thực tập tại Công ty Tinh Vân, tập trung vào lĩnh vực phát triển phần mềm quản lý dự án. Báo cáo mô tả chi tiết về dự án đã tham gia, các công nghệ và công cụ được sử dụng, và các kiến thức và kỹ năng đã học được.\n\nPhần chính của báo cáo tập trung vào việc phát triển một phần mềm quản lý dự án sử dụng Java và PostgreSQL. Phần mềm này cung cấp các chức năng như tạo và quản lý dự án, phân công nhiệm vụ, theo dõi tiến độ, và báo cáo. Báo cáo cũng mô tả về quy trình phát triển phần mềm tại Tinh Vân và kinh nghiệm làm việc trong môi trường doanh nghiệp.', '/uploads/reports/20216678/2024.2/Report_20216678_2024.2_20250602_221101.pdf', DATE_SUB(NOW(), INTERVAL 11 DAY), NOW(), NOW()),

(8, 8, 'Báo cáo thực tập tại FPT Software (chi nhánh quốc tế) - Phát triển ứng dụng di động', 'Báo cáo chi tiết về quá trình thực tập tại FPT Software (chi nhánh quốc tế), tập trung vào lĩnh vực phát triển ứng dụng di động. Báo cáo mô tả chi tiết về dự án đã tham gia, các công nghệ và công cụ được sử dụng, và các kiến thức và kỹ năng đã học được.\n\nPhần chính của báo cáo tập trung vào việc phát triển một ứng dụng di động cho một khách hàng nước ngoài, sử dụng React Native. Ứng dụng này là một nền tảng thương mại điện tử với các chức năng như hiển thị sản phẩm, tìm kiếm, giỏ hàng, và thanh toán. Báo cáo cũng mô tả về các thách thức đã gặp phải khi làm việc trong một dự án quốc tế và các giải pháp đã áp dụng.', '/uploads/reports/20216901/2024.2/Report_20216901_2024.2_20250602_221118.pdf', DATE_SUB(NOW(), INTERVAL 12 DAY), NOW(), NOW());