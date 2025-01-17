package ch.bfh.project1.pwnd.db;

import ch.bfh.project1.pwnd.dao.Breach;
import ch.bfh.project1.pwnd.utils.DateFormatter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BreachHandler {
    private static final String GET_ALL_BREACHES_QUERY = "SELECT * FROM breach";
    private static final String INSERT_BREACH_QUERY = "INSERT OR IGNORE INTO breach(breachName, title, domain, breachDate, addedDate," +
            "modifiedDate, pwnCount, description, logoPath, isVerified," +
            "isFabricated, isSensitive, isRetired, isSpamList, isMalware, isSubscriptionFree)" +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_BREACH_QUERY = "SELECT * FROM breach WHERE breachName = ?";
    private static final Logger logger = LogManager.getLogger(BreachHandler.class);
    public static void addAPIBreaches(Breach[] breaches) {
        try (
                Connection conn = DatabaseHandler.getConnectionHandler();
                PreparedStatement ps = conn.prepareStatement(INSERT_BREACH_QUERY)) {
            conn.setAutoCommit(false);
            for (Breach breach : breaches) {
                ps.setString(1, breach.getBreachName());
                ps.setString(2, breach.getTitle());
                ps.setString(3, breach.getDomain());
                ps.setString(4, DateFormatter.inputToOutputData(breach.getBreachDate()));
                ps.setString(5, DateFormatter.inputToOutputData(breach.getAddedDate()));
                ps.setString(6, DateFormatter.inputToOutputData(breach.getModifiedDate()));
                ps.setInt(7, breach.getPwnCount());
                ps.setString(8, breach.getDescription());
                ps.setString(9, breach.getLogoPath());
                ps.setBoolean(10, breach.isVerified());
                ps.setBoolean(11, breach.isFabricated());
                ps.setBoolean(12, breach.isSensitive());
                ps.setBoolean(13, breach.isRetired());
                ps.setBoolean(14, breach.isSpamList());
                ps.setBoolean(15, breach.isMalware());
                ps.setBoolean(16, breach.isSubscriptionFree());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            logger.info("{} Breaches added successfully", breaches.length);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            System.out.println(e.getMessage());
        }
    }

    public static Breach findBreachByKey(String key) {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_BREACH_QUERY)) {
            ps.setString(1, key);
            try (ResultSet resultSet = ps.executeQuery()) {
                if (resultSet.next()) {
                    return new Breach(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return null;
    }
}