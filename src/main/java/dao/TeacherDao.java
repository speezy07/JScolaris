package dao;

import database.DatabaseConnection;
import model.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherDao implements BaseDao<Teacher> {
    @Override
    public Teacher create(Teacher teacher) throws SQLException {
        String sql = "INSERT INTO teachers (first_name, last_name, specialty, email) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(statement, teacher);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    teacher.setId(keys.getInt(1));
                }
            }
            return teacher;
        }
    }

    @Override
    public boolean update(Teacher teacher) throws SQLException {
        String sql = "UPDATE teachers SET first_name = ?, last_name = ?, specialty = ?, email = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, teacher);
            statement.setInt(5, teacher.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM teachers WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Teacher> findById(int id) throws SQLException {
        String sql = "SELECT * FROM teachers WHERE id = ?";
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
    public List<Teacher> findAll() throws SQLException {
        String sql = "SELECT * FROM teachers ORDER BY last_name, first_name";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapAll(resultSet);
        }
    }

    @Override
    public List<Teacher> search(String keyword) throws SQLException {
        String sql = """
                SELECT * FROM teachers
                WHERE first_name LIKE ? OR last_name LIKE ? OR specialty LIKE ? OR email LIKE ?
                ORDER BY last_name, first_name
                """;
        String pattern = "%" + keyword + "%";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int index = 1; index <= 4; index++) {
                statement.setString(index, pattern);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapAll(resultSet);
            }
        }
    }

    public boolean existsByEmail(String email, Integer ignoredId) throws SQLException {
        String sql = ignoredId == null
                ? "SELECT COUNT(*) FROM teachers WHERE email = ?"
                : "SELECT COUNT(*) FROM teachers WHERE email = ? AND id <> ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            if (ignoredId != null) {
                statement.setInt(2, ignoredId);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM teachers";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    private void fillStatement(PreparedStatement statement, Teacher teacher) throws SQLException {
        statement.setString(1, teacher.getFirstName());
        statement.setString(2, teacher.getLastName());
        statement.setString(3, teacher.getSpecialty());
        statement.setString(4, teacher.getEmail());
    }

    private Teacher map(ResultSet resultSet) throws SQLException {
        return new Teacher(
                resultSet.getInt("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("specialty"),
                resultSet.getString("email")
        );
    }

    private List<Teacher> mapAll(ResultSet resultSet) throws SQLException {
        List<Teacher> teachers = new ArrayList<>();
        while (resultSet.next()) {
            teachers.add(map(resultSet));
        }
        return teachers;
    }
}
