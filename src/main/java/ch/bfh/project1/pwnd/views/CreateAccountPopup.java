package ch.bfh.project1.pwnd.views;

import ch.bfh.project1.pwnd.db.AccountHandler;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CreateAccountPopup {
    public static void showCreateAccountPopup(Runnable updateFunction) {
        JTextField emailField = new JTextField();
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel infoText = new JLabel("Enter an email address for the new account:");
        infoText.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(infoText);
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(
                null,
                panel,
                "Create New Account",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String email = emailField.getText().trim();

            // Validate input
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Email cannot be empty!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                AccountHandler.addAccount(email);
                JOptionPane.showMessageDialog(
                        null,
                        "Account added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                if (updateFunction != null) {
                    updateFunction.run();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Failed to create account: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
