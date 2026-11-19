CREATE DATABASE IF NOT EXISTS jscolaris
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE jscolaris;

CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(30) NOT NULL,
    class_name VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS teachers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    specialty VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS modules (
    id INT AUTO_INCREMENT PRIMARY KEY,
    module_name VARCHAR(120) NOT NULL UNIQUE,
    coefficient DECIMAL(5,2) NOT NULL CHECK (coefficient > 0)
);

CREATE TABLE IF NOT EXISTS subjects (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(120) NOT NULL,
    module_id INT NOT NULL,
    coefficient DECIMAL(5,2) NOT NULL CHECK (coefficient > 0),
    CONSTRAINT uq_subject_module UNIQUE (subject_name, module_id),
    CONSTRAINT fk_subject_module
        FOREIGN KEY (module_id)
        REFERENCES modules(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS grades (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    grade_value DECIMAL(5,2) NOT NULL CHECK (grade_value >= 0 AND grade_value <= 20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uq_grade_student_subject UNIQUE (student_id, subject_id),
    CONSTRAINT fk_grade_student
        FOREIGN KEY (student_id)
        REFERENCES students(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_grade_subject
        FOREIGN KEY (subject_id)
        REFERENCES subjects(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
