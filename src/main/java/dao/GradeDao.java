package dao;

import database.DatabaseConnection;
import model.Grade;
import model.GradeReportRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GradeDao implements BaseDao<Grade> {
    @Override
    public Grade create(Grade grade) throws SQLException {
        String sql = "INSERT INTO grades (student_id, subject_id, grade_value) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(statement, grade);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    grade.setId(keys.getInt(1));
                }
            }
            return grade;
        }
    }

    @Override
    public boolean update(Grade grade) throws SQLException {
        String sql = "UPDATE grades SET student_id = ?, subject_id = ?, grade_value = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, grade);
            statement.setInt(4, grade.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM grades WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Grade> findById(int id) throws SQLException {
        String sql = "SELECT * FROM grades WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Grade> findAll() throws SQLException {
        String sql = "SELECT * FROM grades ORDER BY student_id, subject_id";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapAll(resultSet);
        }
    }

    @Override
    public List<Grade> search(String keyword) throws SQLException {
        String sql = """
                SELECT g.* FROM grades g
                JOIN students st ON st.id = g.student_id
                JOIN subjects su ON su.id = g.subject_id
                WHERE st.first_name LIKE ? OR st.last_name LIKE ? OR su.subject_name LIKE ?
                ORDER BY st.last_name, su.subject_name
                """;
        String pattern = "%" + keyword + "%";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            statement.setString(3, pattern);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapAll(resultSet);
            }
        }
    }

    public Optional<Grade> findByStudentAndSubject(int studentId, int subjectId) throws SQLException {
        String sql = "SELECT * FROM grades WHERE student_id = ? AND subject_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, subjectId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(map(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    public List<GradeReportRow> findReportRowsByStudentId(int studentId) throws SQLException {
        String sql = """
                SELECT g.id AS grade_id,
                       su.id AS subject_id,
                       m.module_name,
                       su.subject_name,
                       m.coefficient AS module_coefficient,
                       su.coefficient AS subject_coefficient,
                       g.grade_value
                FROM grades g
                JOIN subjects su ON su.id = g.subject_id
                JOIN modules m ON m.id = su.module_id
                WHERE g.student_id = ?
                ORDER BY m.module_name, su.subject_name
                """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<GradeReportRow> rows = new ArrayList<>();
                while (resultSet.next()) {
                    rows.add(new GradeReportRow(
                            resultSet.getInt("grade_id"),
                            resultSet.getInt("subject_id"),
                            resultSet.getString("module_name"),
                            resultSet.getString("subject_name"),
                            resultSet.getDouble("module_coefficient"),
                            resultSet.getDouble("subject_coefficient"),
                            resultSet.getDouble("grade_value")
                    ));
                }
                return rows;
            }
        }
    }

    public double calculateAverage(int studentId) throws SQLException {
        List<GradeReportRow> rows = findReportRowsByStudentId(studentId);
        double weightedSum = 0;
        double totalCoefficient = 0;
        for (GradeReportRow row : rows) {
            double coefficient = row.getWeightedCoefficient();
            weightedSum += row.getGradeValue() * coefficient;
            totalCoefficient += coefficient;
        }
        return totalCoefficient == 0 ? 0 : weightedSum / totalCoefficient;
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM grades";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    private void fillStatement(PreparedStatement statement, Grade grade) throws SQLException {
        statement.setInt(1, grade.getStudentId());
        statement.setInt(2, grade.getSubjectId());
        statement.setDouble(3, grade.getGradeValue());
    }

    private Grade map(ResultSet resultSet) throws SQLException {
        return new Grade(
                resultSet.getInt("id"),
                resultSet.getInt("student_id"),
                resultSet.getInt("subject_id"),
                resultSet.getDouble("grade_value")
        );
    }

    private List<Grade> mapAll(ResultSet resultSet) throws SQLException {
        List<Grade> grades = new ArrayList<>();
        while (resultSet.next()) {
            grades.add(map(resultSet));
        }
        return grades;
    }
}
