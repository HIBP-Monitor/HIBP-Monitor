package ch.bfh.project1.pwnd.views;

import ch.bfh.project1.pwnd.model.SettingsModel;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class SettingsView extends JPanel {

    private JButton backButton;
    private JCheckBox enableNotifications;
    private JSlider intervalSlider;
    private JPasswordField apiKeyField;
    private JButton toggleVisibilityButton;
    private JButton saveApiKeyButton;

    public SettingsView() {
        setLayout(new BorderLayout());
        JPanel westPanel = createWestPanel();
        JPanel eastPanel = createEastPanel();
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                westPanel,
                eastPanel
        );
        splitPane.setResizeWeight(0.9); // 80% west, 20% east
        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createWestPanel() {
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
        westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        enableNotifications = new JCheckBox("Enable Notifications");
        enableNotifications.setAlignmentX(Component.LEFT_ALIGNMENT);

        intervalSlider = createRestrictedSlider();
        intervalSlider.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel intervalLabel = new JLabel("API fetch interval:");
        intervalLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        intervalLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // API Key Input Field
        JPanel apiKeyPanel = createApiKeyPanel();
        apiKeyPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        westPanel.add(titleLabel);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing
        westPanel.add(enableNotifications);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(intervalLabel);
        westPanel.add(intervalSlider);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(apiKeyPanel);
        westPanel.add(Box.createVerticalGlue());

        return westPanel;
    }

    private JPanel createApiKeyPanel() {
        JPanel apiKeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JLabel apiKeyLabel = new JLabel("API Key:");
        apiKeyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        apiKeyField = new JPasswordField(20);
        apiKeyField.setEchoChar('●');
        toggleVisibilityButton = new JButton("👁");
        toggleVisibilityButton.setMargin(new Insets(0, 5, 0, 5));
        toggleVisibilityButton.addActionListener(e -> toggleApiKeyVisibility());
        saveApiKeyButton= new JButton("Change API key");
        apiKeyPanel.add(apiKeyLabel);
        apiKeyPanel.add(apiKeyField);
        apiKeyPanel.add(toggleVisibilityButton);
        apiKeyPanel.add(saveApiKeyButton);

        return apiKeyPanel;
    }

    private void toggleApiKeyVisibility() {
        if (apiKeyField.getEchoChar() == '●') {
            apiKeyField.setEchoChar((char) 0);
            toggleVisibilityButton.setText("\uD83D\uDD12");
        } else {
            apiKeyField.setEchoChar('●');
            toggleVisibilityButton.setText("👁");
        }
    }

    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(8, 1, 5, 5));
        backButton = new JButton("Back");
        eastPanel.add(backButton);
        return eastPanel;
    }

    private JSlider createRestrictedSlider() {
        int[] allowedValues = SettingsModel.allowedValues;
        JSlider slider = new JSlider(0, allowedValues.length - 1, 0);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = 0; i < allowedValues.length; i++) {
            labelTable.put(i, new JLabel(allowedValues[i] + "min"));
        }
        slider.setLabelTable(labelTable);
        slider.addChangeListener(e -> {
            int index = slider.getValue();
            slider.setToolTipText("Value: " + allowedValues[index] + "min");
        });

        return slider;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JCheckBox getEnableNotifications() {
        return enableNotifications;
    }

    public JSlider getIntervalSlider() {
        return intervalSlider;
    }

    public JPasswordField getApiKeyField() {
        return apiKeyField;
    }

    public JButton getSaveApiKeyButton() {
        return saveApiKeyButton;
    }
}

