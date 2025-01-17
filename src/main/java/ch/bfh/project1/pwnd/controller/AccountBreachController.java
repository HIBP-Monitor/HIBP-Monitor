package ch.bfh.project1.pwnd.controller;

import ch.bfh.project1.pwnd.dao.AccountBreach;
import ch.bfh.project1.pwnd.db.AccountBreachHandler;
import ch.bfh.project1.pwnd.db.AccountHandler;
import ch.bfh.project1.pwnd.model.AccountBreachModel;
import ch.bfh.project1.pwnd.model.BreachModel;
import ch.bfh.project1.pwnd.model.MainModel;
import ch.bfh.project1.pwnd.utils.BooleanIconRenderer;
import ch.bfh.project1.pwnd.views.AccountBreachView;
import ch.bfh.project1.pwnd.views.BreachView;
import ch.bfh.project1.pwnd.views.MainFrame;
import ch.bfh.project1.pwnd.views.MainView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

public class AccountBreachController {
    private final AccountBreachModel accountBreachModel;
    private final AccountBreachView accountBreachView;
    private final MainFrame mainFrame;
    private static final Logger logger = LogManager.getLogger(AccountBreachController.class);


    public AccountBreachController(AccountBreachView accountBreachView, AccountBreachModel accountBreachModel, MainFrame mainFrame){
        this.accountBreachModel = accountBreachModel;
        this.accountBreachView = accountBreachView;
        this.mainFrame = mainFrame;
        populateTable();
        setTitleLabel();
        setTotalLabel();
        setUnhandledLabel();
        accountBreachView.getBackButton().addActionListener(e -> switchBackToMainView());
        accountBreachView.getDeleteAccountButton().addActionListener(e -> deleteAccount());
        accountBreachView.getHandleAllButton().addActionListener(e -> handleAll());

    }

    private void populateTable() {
        String[] columnNames = {
                "Title", "Domain", "Breach Date", "Added Date",
                "Is Verified", "Is Sensitive", "Is Malware", "Is Handled"
        };

        // Prepare table data
        Object[][] tableData = accountBreachModel.getAccountBreachList().stream()
                .map(breach -> new Object[]{
                        breach.getBreach().getTitle(),
                        breach.getBreach().getDomain(),
                        breach.getBreach().getBreachDate(),
                        breach.getBreach().getAddedDate(),
                        breach.getBreach().isVerified(),
                        breach.getBreach().isSensitive(),
                        breach.getBreach().isMalware(),
                        breach.isHandled() // Checkbox column
                })
                .toArray(Object[][]::new);

        // Custom table model to handle checkbox state
        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 7) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
            @Override
            public void setValueAt(Object aValue, int row, int column) {
                if (column == 7) {
                    boolean isHandled = (boolean) aValue;
                    AccountBreach accountBreach = accountBreachModel.getAccountBreachList().get(row);
                    accountBreach.setHandled(isHandled);
                    try {
                        AccountBreachHandler.updateHandled(
                                accountBreach.getAccount().getEmail(),
                                accountBreach.getBreach().getBreachName(),
                                isHandled
                        );
                    } catch (SQLException e) {
                        logger.error(e.getMessage());
                        JOptionPane.showMessageDialog(
                                null,
                                "Failed to update database: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
                setUnhandledLabel();
                super.setValueAt(aValue, row, column);
            }
        };
        accountBreachView.getTable().setModel(tableModel);
        for (int col : new int[]{4, 5, 6}) { // Indices of other boolean columns
            accountBreachView.getTable().getColumnModel().getColumn(col).setCellRenderer(new BooleanIconRenderer());
        }
        accountBreachView.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable table = (JTable) e.getSource();
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        handleRowSelection(selectedRow);
                    }
                }
            }
        });
    }

    private void setTitleLabel(){
        accountBreachView.getTitleLabel().setText("Breaches for account: " + this.accountBreachModel.getAccount().getEmail());
    }
    private void setTotalLabel(){
        accountBreachView.getTotalLabel().setText("Total breaches: " + (long) this.accountBreachModel.getAccountBreachList().size());
    }
    private void setUnhandledLabel(){
        accountBreachView.getUnhandledLabel().setText("Total unhandled: " + this.accountBreachModel.getAccountBreachList()
                .stream()
                .filter(accountBreach-> !accountBreach.isHandled()).count());
    }
    private void deleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(
                mainFrame,
                "Are you sure you want to delete the account " + this.accountBreachModel.getAccount().getEmail() + "?",
                "Delete Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                AccountHandler.deleteAccount(accountBreachModel.getAccount().getEmail());
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Account " + this.accountBreachModel.getAccount().getEmail() + " deleted successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                switchBackToMainView();

            } catch (SQLException e) {
                logger.error(e.getMessage());
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Failed to delete account: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    private void handleAll() {
        int confirm = JOptionPane.showConfirmDialog(
                mainFrame,
                "Are you sure you want to handle all breaches for the account " + this.accountBreachModel.getAccount().getEmail() + "?",
                "Delete Account",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                AccountBreachHandler.setAllHandled(accountBreachModel.getAccount().getEmail());
                this.accountBreachModel.getAccountBreachList().forEach(accountBreach -> accountBreach.setHandled(true));
                populateTable();

            } catch (SQLException e) {
                logger.error(e.getMessage());
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "Failed to update handled info: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    private void handleRowSelection(int rowIndex) {
        BreachModel breachModel = new BreachModel(this.accountBreachModel.getAccountBreachList().get(rowIndex).getBreach().getBreachName());
        BreachView breachView = new BreachView();
        BreachController breachController = new BreachController(breachView, breachModel, this.mainFrame, this.accountBreachModel.getAccount());
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(breachView);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void switchBackToMainView() {
        mainFrame.getContentPane().removeAll();
        MainModel mainModel = new MainModel();
        MainView mainView = new MainView();
        MainController mainController = new MainController(mainModel, mainView, mainFrame);

        mainFrame.getContentPane().add(mainView);
        mainFrame.revalidate();
        mainFrame.repaint();
    }
}
