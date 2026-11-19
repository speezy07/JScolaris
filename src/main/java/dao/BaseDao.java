package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BaseDao<T> {
    T create(T entity) throws SQLException;

    boolean update(T entity) throws SQLException;

    boolean delete(int id) throws SQLException;

    Optional<T> findById(int id) throws SQLException;

    List<T> findAll() throws SQLException;

    List<T> search(String keyword) throws SQLException;
}
