package ch.bfh.project1.pwnd.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private static String dbPath = "";
    private static final String DB_NAME = "hibp.db";
    private static final Logger logger = LogManager.getLogger(DatabaseHandler.class);
    public static void setUpDB() {
        setDatabasePath();
        createNewDatabase();
        createTables();
    }

    private static void createNewDatabase() {
        try {
            Connection conn = getConnectionHandler();
            if (conn != null) {
                logger.info("A new database has been created or connected at: {}", dbPath);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private static void setDatabasePath() {
        if (dbPath.isBlank()) {
            String osName = System.getProperty("os.name").toLowerCase();
            String baseDir;

            if (osName.contains("win")) {
                baseDir = System.getenv("LOCALAPPDATA"); // Windows: Use LocalAppData
            } else if (osName.contains("mac")) {
                baseDir = System.getProperty("user.home") + "/Library/Application Support"; // macOS
            } else {
                baseDir = System.getProperty("user.home") + "/.local/share"; // Linux and other UNIX-like systems
            }

            File appDir = new File(baseDir, "MonitoringDemon");
            if (!appDir.exists() && !appDir.mkdirs()) {
                logger.error("Failed to create application directory: " + appDir.getAbsolutePath());
                throw new RuntimeException("Failed to create application directory: " + appDir.getAbsolutePath());
            }

            dbPath = appDir.getAbsolutePath() + "/" + DB_NAME;
            logger.info("Database path set to: {}", dbPath);
        }
    }

    private static void createTables() {
        try {
            Connection conn = getConnectionHandler();
            conn.setAutoCommit(false);
            createAccountTable(conn);
            createBreachTable(conn);
            createTypeTable(conn);
            createAccountBreachTable(conn);
            createBreachTypeTable(conn);
            createAppInfoTable(conn);
            AppInfoHandler.setUpDefaultAppInfo(conn);
            conn.commit(); // commit if everything is successful
            logger.info("The database is setup correctly");
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static Connection getConnectionHandler() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        // Enable foreign key constraints
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }


    private static void createAccountTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS account(email TEXT PRIMARY KEY)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private static void createBreachTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS breach ("
                + "breachName TEXT PRIMARY KEY, "
                + "title TEXT, "
                + "domain TEXT, "
                + "breachDate TEXT, "
                + "addedDate TEXT, "
                + "modifiedDate TEXT, "
                + "pwnCount INTEGER, "
                + "description TEXT, "
                + "logoPath TEXT, "
                + "isVerified INTEGER NOT NULL CHECK(isVerified IN (0, 1)) DEFAULT 0, "
                + "isFabricated INTEGER NOT NULL CHECK(isFabricated IN (0, 1)) DEFAULT 0, "
                + "isSensitive INTEGER NOT NULL CHECK(isSensitive IN (0, 1)) DEFAULT 0, "
                + "isRetired INTEGER NOT NULL CHECK(isRetired IN (0, 1)) DEFAULT 0, "
                + "isSpamList INTEGER NOT NULL CHECK(isSpamList IN (0, 1)) DEFAULT 0, "
                + "isMalware INTEGER NOT NULL CHECK(isMalware IN (0, 1)) DEFAULT 0, "
                + "isSubscriptionFree INTEGER NOT NULL CHECK(isSubscriptionFree IN (0, 1)) DEFAULT 0"
                + ");";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    private static void createAccountBreachTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS account_breach ("
                + "email TEXT NOT NULL, "
                + "breach TEXT NOT NULL, "
                + "handled INTEGER NOT NULL CHECK(handled IN (0, 1)) DEFAULT 0, "
                + "PRIMARY KEY (email, breach),"
                + "FOREIGN KEY(email) REFERENCES account(email) ON DELETE CASCADE, "
                + "FOREIGN KEY(breach) REFERENCES breach(breachName))";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    private static void createTypeTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS type ("
                + "type TEXT PRIMARY KEY)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    private static void createBreachTypeTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS breach_type ("
                + "type TEXT NOT NULL, "
                + "breach TEXT NOT NULL, "
                + "PRIMARY KEY (type, breach),"
                + "FOREIGN KEY(type) REFERENCES type(type), "
                + "FOREIGN KEY(breach) REFERENCES breach(breachName))";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
    private static void createAppInfoTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS app_info ("
                + "name TEXT PRIMARY KEY,"
                + "value TEXT NOT NULL)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

}
