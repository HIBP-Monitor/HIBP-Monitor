package ch.bfh.project1.pwnd.views;


import javax.swing.*;
import java.awt.*;


public class AccountBreachView extends JPanel {

    private JTable table;
    private JLabel titleLabel;
    private JLabel totalLabel;
    private JLabel unhandledLabel;
    private JButton backButton;
    private JButton deleteAccountButton;
    private JButton handleAllButton;
    public AccountBreachView(){
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
        totalLabel = createTotalLabel();
        unhandledLabel = createUnhandledLabel();
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(titleLabel);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(totalLabel);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        westPanel.add(unhandledLabel);
        westPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        JScrollPane tableScrollPane = createTable();
        westPanel.add(tableScrollPane);
        return westPanel;
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("" , JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        return titleLabel;
    }
    private JLabel createTotalLabel() {
        JLabel totalLabel = new JLabel("" , JLabel.LEFT);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        return totalLabel;
    }
    private JLabel createUnhandledLabel() {
        JLabel unhandledLabel = new JLabel("" , JLabel.LEFT);
        unhandledLabel.setFont(new Font("Arial", Font.BOLD, 14));
        return unhandledLabel;
    }

    private JScrollPane createTable() {
        table = new JTable();
        return new JScrollPane(table);
    }

    private JPanel createEastPanel() {
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(8, 1, 5, 5));

        backButton = new JButton("Back");
        deleteAccountButton = new JButton("Delete Account");
        handleAllButton = new JButton("Handle all");
        eastPanel.add(backButton);
        eastPanel.add(deleteAccountButton);
        eastPanel.add(handleAllButton);
        return eastPanel;
    }

    public JTable getTable() {
        return table;
    }

    public JLabel getTitleLabel(){return titleLabel;}

    public JLabel getTotalLabel() {
        return totalLabel;
    }

    public JLabel getUnhandledLabel() {
        return unhandledLabel;
    }

    public JButton getBackButton() {
        return backButton;
    }

    public JButton getDeleteAccountButton() {
        return deleteAccountButton;
    }

    public JButton getHandleAllButton() {return handleAllButton;}
}

