package ch.bfh.project1.pwnd.controller;

import ch.bfh.project1.pwnd.listener.ApiCallListener;
import ch.bfh.project1.pwnd.listener.MonitoringDemonNotifier;
import ch.bfh.project1.pwnd.model.AccountBreachModel;
import ch.bfh.project1.pwnd.model.MainModel;
import ch.bfh.project1.pwnd.model.SettingsModel;
import ch.bfh.project1.pwnd.views.*;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class MainController implements ApiCallListener {

    private final MainModel mainModel;
    private final MainView mainView;
    private final MainFrame mainFrame;
    private static final Logger logger = LogManager.getLogger(MainController.class);

    public MainController(MainModel mainModel, MainView mainView, MainFrame mainFrame) {
        this.mainModel = mainModel;
        this.mainView = mainView;
        this.mainFrame = mainFrame;
        MonitoringDemonNotifier.getInstance().addApiCallListener(this);
        populateTable();
        this.mainView.getLastApiCallLabel().setText(this.mainModel.getLastApiCall());
        this.mainView.getAddAccountButton().addActionListener(e ->
                CreateAccountPopup.showCreateAccountPopup(() -> {
                    this.mainModel.update();
                    this.populateTable();
                    MonitoringDemonNotifier.getInstance().notifyNewAccountListeners();
                })
        );
        this.mainView.getChangeThemeButton().addActionListener(e -> this.switchTheme());
        this.mainView.getOpenSettingsButton().addActionListener(e -> this.openSettings());
    }
    private void populateTable() {
        String[] columnNames = {"Account", "Breaches Count", "Unhandled Breaches"};
        Object[][] tableData = mainModel.getMainTableEntryList().stream()
                .map(entry -> new Object[]{
                        entry.account().getEmail(),
                        entry.breaches().length,
                        entry.unhandled()
                })
                .toArray(Object[][]::new);

        DefaultTableModel tableModel = new DefaultTableModel(tableData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        mainView.getTable().setModel(tableModel);
        mainView.getTable().addMouseListener(new java.awt.event.MouseAdapter() {
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

    private void handleRowSelection(int rowIndex) {
        MainModel.MainTableEntry entry = mainModel.getMainTableEntryList().get(rowIndex);
        AccountBreachModel accountBreachModel = new AccountBreachModel(entry.account(), this.mainModel.getAccountBreachMap().get(entry.account().getEmail()));
        AccountBreachView accountBreachView = new AccountBreachView();
        AccountBreachController accountBreachController = new AccountBreachController(accountBreachView, accountBreachModel, this.mainFrame);
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(accountBreachView);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void openSettings() {

        SettingsModel settingsModel = new SettingsModel();
        SettingsView settingsView = new SettingsView();
        SettingsController settingsController = new SettingsController(settingsView, settingsModel, mainFrame);
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(settingsView);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void switchTheme() {
        try {
            if(UIManager.getLookAndFeel() instanceof FlatLightLaf){
                UIManager.setLookAndFeel(new FlatDarkLaf());
                changeTitleIcon("dark");
            } else {
                UIManager.setLookAndFeel(new FlatLightLaf());
                changeTitleIcon("light");
            }
            SwingUtilities.updateComponentTreeUI(mainFrame);
        } catch (UnsupportedLookAndFeelException e) {
            logger.error(e.getMessage());
            JOptionPane.showMessageDialog(mainFrame, "Failed to apply theme", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void changeTitleIcon(String iconName){
        ImageIcon resizedIcon;
        try {
            ImageIcon originalIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/" + iconName + ".png")));
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            resizedIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            logger.error("Warning: Unable to load icon. {}", e.getMessage());
            resizedIcon = null;
        }
        mainView.getTitleLabel().setIcon(resizedIcon);
    }

    @Override
    public void onApiCalled() {
        this.mainModel.update();
        this.populateTable();
        this.mainView.getLastApiCallLabel().setText(this.mainModel.getLastApiCall());
    }
}
