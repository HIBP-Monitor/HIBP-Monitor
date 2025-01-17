package ch.bfh.project1.pwnd.db;

import ch.bfh.project1.pwnd.MonitoringDemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BreachTypeHandler {

    private static final String INSERT_BREACH_TYPE = "INSERT OR IGNORE INTO breach_type(breach,type) VALUES(?,?)";
    private static final String FIND_ALL_TYPES_BY_BREACH = "SELECT type FROM breach_type WHERE breach=?";
    private static final Logger logger = LogManager.getLogger(MonitoringDemon.class);

    public static void addBreachType(String[] types, String breach) {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(INSERT_BREACH_TYPE)) {
            conn.setAutoCommit(false);
            for (String type : types) {
                ps.setString(1, breach);
                ps.setString(2, type);
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }

    public static List<String> findAllTypesByBreach(String breach) {
        List<String> types = new ArrayList<>();
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_TYPES_BY_BREACH)) {
            ps.setString(1, breach);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    types.add(resultSet.getString("type"));
                }
                return types;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}
