package dao;

import database.DatabaseConnection;
import model.Subject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SubjectDao implements BaseDao<Subject> {
    @Override
    public Subject create(Subject subject) throws SQLException {
        String sql = "INSERT INTO subjects (subject_name, module_id, coefficient) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(statement, subject);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    subject.setId(keys.getInt(1));
                }
            }
            return subject;
        }
    }

    @Override
    public boolean update(Subject subject) throws SQLException {
        String sql = "UPDATE subjects SET subject_name = ?, module_id = ?, coefficient = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, subject);
            statement.setInt(4, subject.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM subjects WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Subject> findById(int id) throws SQLException {
        String sql = "SELECT * FROM subjects WHERE id = ?";
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
    public List<Subject> findAll() throws SQLException {
        String sql = "SELECT * FROM subjects ORDER BY subject_name";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapAll(resultSet);
        }
    }

    @Override
    public List<Subject> search(String keyword) throws SQLException {
        String sql = """
                SELECT s.* FROM subjects s
                JOIN modules m ON m.id = s.module_id
                WHERE s.subject_name LIKE ? OR m.module_name LIKE ?
                ORDER BY s.subject_name
                """;
        String pattern = "%" + keyword + "%";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, pattern);
            statement.setString(2, pattern);
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapAll(resultSet);
            }
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM subjects";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    private void fillStatement(PreparedStatement statement, Subject subject) throws SQLException {
        statement.setString(1, subject.getSubjectName());
        statement.setInt(2, subject.getModuleId());
        statement.setDouble(3, subject.getCoefficient());
    }

    private Subject map(ResultSet resultSet) throws SQLException {
        return new Subject(
                resultSet.getInt("id"),
                resultSet.getString("subject_name"),
                resultSet.getInt("module_id"),
                resultSet.getDouble("coefficient")
        );
    }

    private List<Subject> mapAll(ResultSet resultSet) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        while (resultSet.next()) {
            subjects.add(map(resultSet));
        }
        return subjects;
    }
}
