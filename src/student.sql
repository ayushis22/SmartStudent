-- Create the database (if not already created)
CREATE DATABASE IF NOT EXISTS smartstudent;
USE smartstudent;

-- Table for storing admin credentials
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- Insert default admin credentials
INSERT INTO admins (username, password)
SELECT 'admin', 'admin123'
    WHERE NOT EXISTS (
    SELECT 1 FROM admins WHERE username = 'admin'
);


-- Table for storing student information
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    roll_no VARCHAR(20) NOT NULL UNIQUE,
    department VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    subject1 INT CHECK (subject1 BETWEEN 0 AND 100),
    subject2 INT CHECK (subject2 BETWEEN 0 AND 100),
    subject3 INT CHECK (subject3 BETWEEN 0 AND 100)
);

-- Sample student data (optional)
INSERT INTO students (name, roll_no, department, email, phone, subject1, subject2, subject3)
SELECT 'Amit Kumar', 'CS101', 'Computer Science', 'amit@example.com', '9876543210', 85, 78, 92
    WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE roll_no = 'CS101'
);

INSERT INTO students (name, roll_no, department, email, phone, subject1, subject2, subject3)
SELECT 'Sneha Sharma', 'BT102', 'Biotechnology', 'sneha@example.com', '9123456780', 90, 91, 88
    WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE roll_no = 'BT102'
);

INSERT INTO students (name, roll_no, department, email, phone, subject1, subject2, subject3)
SELECT 'Ravi Patel', 'ME103', 'Mechanical', 'ravi@example.com', '9001234567', 60, 55, 70
    WHERE NOT EXISTS (
    SELECT 1 FROM students WHERE roll_no = 'ME103'
);