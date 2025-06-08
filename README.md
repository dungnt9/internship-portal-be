# Internship Portal Backend

Hệ thống quản lý thực tập doanh nghiệp cho trường đại học, được xây dựng bằng Spring Boot với kiến trúc microservices.

## Yêu cầu

- Java 17+
- Maven 3.6+
- MySQL 8.0+

## Chuẩn bị môi trường

### 1. Tải và cài đặt

**Java 17:**
- Tải từ: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- Kiểm tra: `java -version`

**Maven:**
- Tải từ: https://maven.apache.org/download.cgi
- Kiểm tra: `mvn -version`

**MySQL:**
- Tải từ: https://dev.mysql.com/downloads/mysql/
- Kiểm tra: `mysql --version`

### 2. Chuẩn bị Database

**Kết nối MySQL:**
```bash
mysql -u root -p

### 3. Chạy ứng dụng

```bash
mvn clean install

Chạy từng service
```bash
mvn spring-boot:run
