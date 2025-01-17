package ch.bfh.project1.pwnd.db;

import ch.bfh.project1.pwnd.dao.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountHandler {
    private static final String GET_ALL_ACCOUNTS_QUERY = "SELECT * FROM account";
    private static final String INSERT_ACCOUNT_QUERY = "INSERT OR IGNORE INTO account(email) VALUES(?)";
    private static final String DELETE_ACCOUNT_QUERY = "DELETE FROM account WHERE email = ?";
    private static final String UPDATE_ACCOUNT_QUERY = "UPDATE account SET email = ? WHERE email = ?";
    private static final Logger logger = LogManager.getLogger(AccountHandler.class);


    public static void addAccount(String email) throws SQLException {
        try(Connection conn = DatabaseHandler.getConnectionHandler();
            PreparedStatement ps = conn.prepareStatement(INSERT_ACCOUNT_QUERY);) {
            ps.setString(1, email);
            ps.executeUpdate();
            logger.info("Added account: {}", email);
        }
    }

    public static void deleteAccount(String email) throws SQLException {
        try(Connection conn = DatabaseHandler.getConnectionHandler();
            PreparedStatement ps = conn.prepareStatement(DELETE_ACCOUNT_QUERY);) {
            ps.setString(1, email);
            ps.executeUpdate();
            logger.info("Deleted account: {}", email);

        }
    }

    public static List<Account> findAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try {
            Statement stmt = DatabaseHandler.getConnectionHandler().createStatement();
            ResultSet rs = stmt.executeQuery(GET_ALL_ACCOUNTS_QUERY);
            while (rs.next()) {
                Account account = new Account(rs);
                accounts.add(account);
            }
        } catch (SQLException e) {
            logger.error("Error when finding all accounts",e);
            throw new RuntimeException(e);
        }
        return accounts;
    }
}
