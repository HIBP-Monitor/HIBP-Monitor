package ch.bfh.project1.pwnd.db;

import ch.bfh.project1.pwnd.enums.AppInfoKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AppInfoHandler {

    private static final String INSERT_INTO_APP_INFO = "INSERT OR IGNORE INTO app_info(name, value) VALUES(?,?)";
    private static final String UPDATE_APP_INFO = "UPDATE app_info SET value = ? WHERE name = ?";
    private static final String INCREMENT_API_CALLS =
            "UPDATE app_info SET value = CAST(value AS INTEGER) + 1 WHERE name = '" + AppInfoKey.API_CALLS.name + "'";
    private static final String GET_VALUE_BY_NAME = "SELECT value FROM app_info WHERE name = ?";
    private static final Logger logger = LogManager.getLogger(AppInfoHandler.class);


    private static final HashMap<String, String> defaultAppInfo = new HashMap<>() {{
        put(AppInfoKey.API_CALLS.name, "0");
        put(AppInfoKey.NOTIFICATIONS.name, "1");
        put(AppInfoKey.INTERVAL.name, "2");
        put(AppInfoKey.API_KEY.name, "");
        put(AppInfoKey.LAST_API_CALL.name, "");
    }};

    public static void setUpDefaultAppInfo(Connection conn) {
        try (PreparedStatement ps = conn.prepareStatement(INSERT_INTO_APP_INFO)) {
            conn.setAutoCommit(false);
            for (Map.Entry<String, String> entry : defaultAppInfo.entrySet()) {
                ps.setString(1, entry.getKey());
                ps.setString(2, entry.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();

        } catch (SQLException e) {
            logger.error("Failed to set up default statistics", e);
            throw new RuntimeException("Failed to set up default statistics", e);
        }
    }

    public static void updateAppInfo(String name, String value) {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(UPDATE_APP_INFO)) {
            ps.setString(1, value);
            ps.setString(2, name);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Failed to update statistic with name: {}", name, e);
            throw new RuntimeException("Failed to update statistic with name: " + name, e);
        }
    }

    public static int getAppInfoValueInt(String name) {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(GET_VALUE_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Integer.parseInt(rs.getString(1));
                } else {
                    logger.error("No value found for name: {}", name);
                    throw new RuntimeException("No value found for name: " + name);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to get app info with name: {}", name, e);
            throw new RuntimeException("Failed to get app info with name: " + name, e);
        }
    }

    public static String getAppInfoValueString(String name) {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(GET_VALUE_BY_NAME)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                } else {
                    logger.error("No value found for name: {}", name);
                    throw new RuntimeException("No value found for name: " + name);
                }
            }
        } catch (SQLException e) {
            logger.error("Failed to get app info with name: {}", name, e);
            throw new RuntimeException("Failed to get app info with name: " + name, e);
        }
    }


    public static void incrementAPICallsCount() {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(INCREMENT_API_CALLS)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to increment api calls, {}", e.getMessage(), e);
            throw new RuntimeException("Failed to increment api calls", e);
        }
    }


}
