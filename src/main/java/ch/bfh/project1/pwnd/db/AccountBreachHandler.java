package ch.bfh.project1.pwnd.db;

import ch.bfh.project1.pwnd.dao.Account;
import ch.bfh.project1.pwnd.dao.AccountBreach;
import ch.bfh.project1.pwnd.dao.Breach;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountBreachHandler {

    private static final Logger logger = LogManager.getLogger(AccountBreachHandler.class);
    private static final String INSERT_ACCOUNT_BREACH = "INSERT OR IGNORE INTO account_breach(email,breach, handled) VALUES(?,?,0)";
    private static final String GET_ALL_UNHANDLED = "SELECT * FROM account_breach WHERE handled = 0";
    private static final String UPDATE_HANDLED = "UPDATE account_breach SET HANDLED = ? WHERE email = ? AND breach = ?";
    private static final String FIND_ALL_BREACHES_BY_ACCOUNT = "SELECT b.*, ab.handled FROM account_breach ab JOIN breach b ON ab.breach = b.breachName WHERE ab.email = ?";
    private static final String SET_ALL_HANDLED_BY_EMAIL = "UPDATE account_breach SET HANDLED = 1 WHERE email = ?";
    private static final String FIND_ALL_AFFECTED_ACCOUNTS_BY_BREACH = "SELECT email from account_breach WHERE breach = ?";
    public static void addAccountBreach(String[] breaches, String email) {
        try (Connection conn = DatabaseHandler.getConnectionHandler();
             PreparedStatement ps = conn.prepareStatement(INSERT_ACCOUNT_BREACH)) {
            conn.setAutoCommit(false);
            for (String breach : breaches) {
                ps.setString(1, email);
                ps.setString(2, breach);
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            logger.error("Error when adding account{}",e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static List<String> findAllUnhandeld() {
        List<String> unhandled = new ArrayList<>();
        try (
                Connection conn = DatabaseHandler.getConnectionHandler();
                PreparedStatement stmt = conn.prepareStatement(GET_ALL_UNHANDLED)
        ) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                unhandled.add(rs.getString("breach"));
            }
        } catch (SQLException e) {
            logger.error("Error when trying to find unhandled breaches{}",e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return unhandled;
    }

    public static List<String> findAllAccountsAffectedByBreach(String breach) {
        List<String> affectedAccounts = new ArrayList<>();
        try (
                Connection conn = DatabaseHandler.getConnectionHandler();
                PreparedStatement stmt = conn.prepareStatement(FIND_ALL_AFFECTED_ACCOUNTS_BY_BREACH)
        ) {
            stmt.setString(1, breach);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                affectedAccounts.add(rs.getString("email"));
            }
        } catch (SQLException e) {
            logger.error("Error when trying to find all accounts affected by the breach{}{}", breach, e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return affectedAccounts;
    }

    public static List<AccountBreach> findAllBreachesByAccount(String email) {
        List<AccountBreach> accountBreaches = new ArrayList<>();

        try (Connection connection = DatabaseHandler.getConnectionHandler();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_BREACHES_BY_ACCOUNT)) {

            // Set the parameter for the prepared statement
            statement.setString(1, email);

            // Execute the query
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Map the result to a Breach object
                    Breach breach = new Breach();
                    breach.setBreachName(resultSet.getString("breachName"));
                    breach.setTitle(resultSet.getString("title"));
                    breach.setDomain(resultSet.getString("domain"));
                    breach.setBreachDate(resultSet.getString("breachDate"));
                    breach.setAddedDate(resultSet.getString("addedDate"));
                    breach.setModifiedDate(resultSet.getString("modifiedDate"));
                    breach.setPwnCount(resultSet.getInt("pwnCount"));
                    breach.setDescription(resultSet.getString("description"));
                    breach.setLogoPath(resultSet.getString("logoPath"));
                    breach.setVerified(resultSet.getBoolean("isVerified"));
                    breach.setFabricated(resultSet.getBoolean("isFabricated"));
                    breach.setSensitive(resultSet.getBoolean("isSensitive"));
                    breach.setRetired(resultSet.getBoolean("isRetired"));
                    breach.setSpamList(resultSet.getBoolean("isSpamList"));
                    breach.setMalware(resultSet.getBoolean("isMalware"));
                    breach.setSubscriptionFree(resultSet.getBoolean("isSubscriptionFree"));
                    boolean handled = resultSet.getBoolean("handled");
                    AccountBreach accountBreach = new AccountBreach();
                    accountBreach.setAccount(new Account(email));
                    accountBreach.setBreach(breach);
                    accountBreach.setHandled(handled);

                    accountBreaches.add(accountBreach);
                }
            }
        } catch (SQLException e) {
            logger.error("SQL error while fetching breaches for account:{}{}", email, e.getMessage(), e);
        }
        return accountBreaches;
    }
    public static void updateHandled(String email, String breachName, boolean handled) throws SQLException {
        try (Connection connection = DatabaseHandler.getConnectionHandler();
             PreparedStatement statement = connection.prepareStatement(UPDATE_HANDLED)) {
            statement.setBoolean(1, handled);
            statement.setString(2, email);
            statement.setString(3, breachName);
            statement.executeUpdate();
        }
    }
    public static void setAllHandled(String email) throws SQLException {
        try (Connection connection = DatabaseHandler.getConnectionHandler();
             PreparedStatement statement = connection.prepareStatement(SET_ALL_HANDLED_BY_EMAIL)) {
            statement.setString(1, email);
            statement.executeUpdate();
        }
    }

}
