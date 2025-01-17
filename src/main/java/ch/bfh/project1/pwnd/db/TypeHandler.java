package ch.bfh.project1.pwnd.db;

import ch.bfh.project1.pwnd.MonitoringDemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TypeHandler {

    private static final String GET_ALL_TYPES_QUERY = "SELECT * FROM type";
    private static final String INSERT_TYPE = "INSERT OR IGNORE INTO type(type) VALUES(?)";
    private static final Logger logger = LogManager.getLogger(TypeHandler.class);

    public static List<String> findAllTypes() {
        List<String> types = new ArrayList<>();
        try (
                Statement stmt = DatabaseHandler.getConnectionHandler().createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL_TYPES_QUERY)) {
            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
        return types;
    }

    public static void addTypes(String[] types) {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(INSERT_TYPE)) {
            conn.setAutoCommit(false);
            for (String type : types) {
                ps.setString(1, type);
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException(e);
        }
    }
}
