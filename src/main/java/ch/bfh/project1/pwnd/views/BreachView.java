package ch.bfh.project1.pwnd.views;

import javax.swing.*;
import java.awt.*;

public class BreachView extends JPanel {

    private JButton backButton;
    private JLabel nameLabel;
    private JLabel domainLabel;
    private JLabel breachDateLabel;
    private JLabel addedDateLabel;
    private JLabel modifiedDateLabel;
    private JLabel pwnCountLabel;
    private JScrollPane descriptionScrollPane;
    private JScrollPane breachTypesScrollPane;
    private JScrollPane affectedAccountsScrollPane;

    public BreachView() {
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
        westPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JLabel title = new JLabel("Breach Details");
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 18));


        nameLabel = createAlignedLabel();
        domainLabel = createAlignedLabel();
        breachDateLabel = createAlignedLabel();
        addedDateLabel = createAlignedLabel();
        modifiedDateLabel = createAlignedLabel();
        pwnCountLabel = createAlignedLabel();

        descriptionScrollPane = createScrollPane("Description");
        breachTypesScrollPane = createScrollPane("Leaked Information");
        affectedAccountsScrollPane = createScrollPane("Your Affected Accounts");

        westPanel.add(title);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(nameLabel);
        westPanel.add(domainLabel);
        westPanel.add(breachDateLabel);
        westPanel.add(addedDateLabel);
        westPanel.add(modifiedDateLabel);
        westPanel.add(pwnCountLabel);

        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(descriptionScrollPane);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(breachTypesScrollPane);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(affectedAccountsScrollPane);

        return westPanel;
    }

    private JScrollPane createScrollPane(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        return scrollPane;
    }

    private JLabel createAlignedLabel() {
        JLabel label = new JLabel();
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(8, 1, 5, 5));
        backButton = new JButton("Back");
        eastPanel.add(backButton);
        return eastPanel;
    }

    public void setName(String title) {
        nameLabel.setText("<html><b>Name:</b> " + title + "</html>");
    }


    public JButton getBackButton() {
        return backButton;
    }

    public JLabel getNameLabel() {
        return nameLabel;
    }

    public JLabel getDomainLabel() {
        return domainLabel;
    }

    public JLabel getBreachDateLabel() {
        return breachDateLabel;
    }

    public JLabel getAddedDateLabel() {
        return addedDateLabel;
    }

    public JLabel getModifiedDateLabel() {
        return modifiedDateLabel;
    }

    public JLabel getPwnCountLabel() {
        return pwnCountLabel;
    }

    public JScrollPane getDescriptionScrollPane() {
        return descriptionScrollPane;
    }

    public JScrollPane getBreachTypesScrollPane() {
        return breachTypesScrollPane;
    }

    public JScrollPane getAffectedAccountsScrollPane() {
        return affectedAccountsScrollPane;
    }
}
