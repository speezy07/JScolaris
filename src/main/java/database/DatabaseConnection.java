package database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {
    private static final String PROPERTIES_FILE = "/database.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream inputStream = DatabaseConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream != null) {
                PROPERTIES.load(inputStream);
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException exception) {
            throw new ExceptionInInitializerError("Unable to load database properties: " + exception.getMessage());
        } catch (ClassNotFoundException exception) {
            throw new ExceptionInInitializerError("MySQL JDBC driver not found: " + exception.getMessage());
        }
    }

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        String url = setting(
                "JSCOLARIS_DB_URL",
                "db.url",
                "jdbc:mysql://localhost:3306/jscolaris?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );
        String user = setting("JSCOLARIS_DB_USER", "db.user", "root");
        String password = setting("JSCOLARIS_DB_PASSWORD", "db.password", "");
        return DriverManager.getConnection(url, user, password);
    }

    private static String setting(String environmentVariable, String propertyName, String defaultValue) {
        String value = System.getenv(environmentVariable);
        return value != null ? value : PROPERTIES.getProperty(propertyName, defaultValue);
    }
}
