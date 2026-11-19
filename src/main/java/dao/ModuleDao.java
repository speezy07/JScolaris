package dao;

import database.DatabaseConnection;
import model.SchoolModule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleDao implements BaseDao<SchoolModule> {
    @Override
    public SchoolModule create(SchoolModule module) throws SQLException {
        String sql = "INSERT INTO modules (module_name, coefficient) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillStatement(statement, module);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    module.setId(keys.getInt(1));
                }
            }
            return module;
        }
    }

    @Override
    public boolean update(SchoolModule module) throws SQLException {
        String sql = "UPDATE modules SET module_name = ?, coefficient = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            fillStatement(statement, module);
            statement.setInt(3, module.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM modules WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<SchoolModule> findById(int id) throws SQLException {
        String sql = "SELECT * FROM modules WHERE id = ?";
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
    public List<SchoolModule> findAll() throws SQLException {
        String sql = "SELECT * FROM modules ORDER BY module_name";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return mapAll(resultSet);
        }
    }

    @Override
    public List<SchoolModule> search(String keyword) throws SQLException {
        String sql = "SELECT * FROM modules WHERE module_name LIKE ? ORDER BY module_name";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + keyword + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                return mapAll(resultSet);
            }
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM modules";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    private void fillStatement(PreparedStatement statement, SchoolModule module) throws SQLException {
        statement.setString(1, module.getModuleName());
        statement.setDouble(2, module.getCoefficient());
    }

    private SchoolModule map(ResultSet resultSet) throws SQLException {
        return new SchoolModule(
                resultSet.getInt("id"),
                resultSet.getString("module_name"),
                resultSet.getDouble("coefficient")
        );
    }

    private List<SchoolModule> mapAll(ResultSet resultSet) throws SQLException {
        List<SchoolModule> modules = new ArrayList<>();
        while (resultSet.next()) {
            modules.add(map(resultSet));
        }
        return modules;
    }
}
