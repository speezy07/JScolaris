USE jscolaris;

INSERT INTO students (first_name, last_name, email, phone, class_name) VALUES
('Yasmine', 'Benali', 'yasmine.benali@example.com', '000-000-0101', 'CS3-A'),
('Omar', 'El Idrissi', 'omar.elidrissi@example.com', '000-000-0102', 'CS3-A'),
('Sara', 'Mansouri', 'sara.mansouri@example.com', '000-000-0103', 'CS3-B')
ON DUPLICATE KEY UPDATE first_name = VALUES(first_name);

INSERT INTO teachers (first_name, last_name, specialty, email) VALUES
('Nadia', 'Rami', 'Databases', 'nadia.rami@example.com'),
('Karim', 'Berrada', 'Software Engineering', 'karim.berrada@example.com'),
('Hind', 'Lahlou', 'Networks', 'hind.lahlou@example.com')
ON DUPLICATE KEY UPDATE specialty = VALUES(specialty);

INSERT INTO modules (module_name, coefficient) VALUES
('Software Engineering', 3.00),
('Data Systems', 3.00),
('Computer Networks', 2.00)
ON DUPLICATE KEY UPDATE coefficient = VALUES(coefficient);

INSERT INTO subjects (subject_name, module_id, coefficient)
SELECT 'Java Programming', id, 2.00 FROM modules WHERE module_name = 'Software Engineering'
ON DUPLICATE KEY UPDATE coefficient = VALUES(coefficient);

INSERT INTO subjects (subject_name, module_id, coefficient)
SELECT 'UML and Design Patterns', id, 1.50 FROM modules WHERE module_name = 'Software Engineering'
ON DUPLICATE KEY UPDATE coefficient = VALUES(coefficient);

INSERT INTO subjects (subject_name, module_id, coefficient)
SELECT 'Relational Databases', id, 2.00 FROM modules WHERE module_name = 'Data Systems'
ON DUPLICATE KEY UPDATE coefficient = VALUES(coefficient);

INSERT INTO subjects (subject_name, module_id, coefficient)
SELECT 'Network Administration', id, 1.50 FROM modules WHERE module_name = 'Computer Networks'
ON DUPLICATE KEY UPDATE coefficient = VALUES(coefficient);

INSERT INTO grades (student_id, subject_id, grade_value)
SELECT st.id, su.id, 16.50
FROM students st
JOIN subjects su ON su.subject_name = 'Java Programming'
WHERE st.email = 'yasmine.benali@example.com'
ON DUPLICATE KEY UPDATE grade_value = VALUES(grade_value);

INSERT INTO grades (student_id, subject_id, grade_value)
SELECT st.id, su.id, 14.75
FROM students st
JOIN subjects su ON su.subject_name = 'Relational Databases'
WHERE st.email = 'yasmine.benali@example.com'
ON DUPLICATE KEY UPDATE grade_value = VALUES(grade_value);

INSERT INTO grades (student_id, subject_id, grade_value)
SELECT st.id, su.id, 13.25
FROM students st
JOIN subjects su ON su.subject_name = 'Java Programming'
WHERE st.email = 'omar.elidrissi@example.com'
ON DUPLICATE KEY UPDATE grade_value = VALUES(grade_value);
