package ch.bfh.project1.pwnd.views;

import ch.bfh.project1.pwnd.MonitoringDemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainView extends JPanel {
    private static final Logger logger = LogManager.getLogger(MainView.class);

    private JTable table;
    private JButton addAccountButton;
    private JButton openSettingsButton;
    private JButton changeThemeButton;
    private JLabel lastApiCallLabel;


    private JLabel titleLabel;

    public MainView() {
        setLayout(new BorderLayout());
        JPanel westPanel = createWestPanel();
        JPanel eastPanel = createEastPanel();
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                westPanel,
                eastPanel
        );
        splitPane.setResizeWeight(0.9); // 90% west, 10% east
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        titleLabel = createTitleLabel();
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(titleLabel);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JScrollPane tableScrollPane = createTable();
        westPanel.add(tableScrollPane);
        return westPanel;
    }

    private JScrollPane createTable() {
        table = new JTable();
        return new JScrollPane(table);
    }
    private JLabel createTitleLabel() {
        ImageIcon resizedIcon;
        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/dark.png")));
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            logger.error("Warning: Unable to load icon. {}", e.getMessage(), e);
            resizedIcon = null;
        }
        JLabel titleLabel = new JLabel("Have I been PWND monitoring demon", resizedIcon, JLabel.CENTER);
        titleLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        titleLabel.setVerticalTextPosition(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titleLabel;
    }

    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(8, 1, 5, 5));
        addAccountButton = new JButton("Add Account");
        openSettingsButton = new JButton("Open Settings");
        changeThemeButton = new JButton("Switch Theme");
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        JLabel updateTextLabel = new JLabel("Last API call:");
        updateTextLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        lastApiCallLabel = new JLabel("");
        lastApiCallLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        labelPanel.add(updateTextLabel);
        labelPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        labelPanel.add(lastApiCallLabel);
        updateTextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        lastApiCallLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        eastPanel.add(addAccountButton);
        eastPanel.add(openSettingsButton);
        eastPanel.add(changeThemeButton);
        eastPanel.add(labelPanel);

        return eastPanel;
    }


    public JTable getTable() {
        return table;
    }

    public JButton getAddAccountButton() {
        return addAccountButton;
    }

    public JButton getChangeThemeButton() {
        return changeThemeButton;
    }

    public JLabel getTitleLabel() {
        return titleLabel;
    }

    public JButton getOpenSettingsButton() {return openSettingsButton;}

    public JLabel getLastApiCallLabel() {
        return lastApiCallLabel;
    }
}
