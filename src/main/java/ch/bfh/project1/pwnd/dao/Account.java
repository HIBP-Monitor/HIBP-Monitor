package ch.bfh.project1.pwnd.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    private String email;

    public Account(ResultSet rs) throws SQLException {
        email = rs.getString("email");
    }
    public Account(String email) throws SQLException {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}