package ch.bfh.project1.pwnd.controller;

import ch.bfh.project1.pwnd.db.AppInfoHandler;
import ch.bfh.project1.pwnd.enums.AppInfoKey;
import ch.bfh.project1.pwnd.listener.MonitoringDemonNotifier;
import ch.bfh.project1.pwnd.model.MainModel;
import ch.bfh.project1.pwnd.model.SettingsModel;
import ch.bfh.project1.pwnd.views.MainFrame;
import ch.bfh.project1.pwnd.views.MainView;
import ch.bfh.project1.pwnd.views.SettingsView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;

public class SettingsController {

    SettingsView settingsView;
    SettingsModel settingsModel;
    MainFrame mainFrame;
    private static final Logger logger = LogManager.getLogger(SettingsController.class);

    public SettingsController(SettingsView settingsView, SettingsModel settingsModel, MainFrame mainFrame){
        this.settingsView = settingsView;
        this.settingsModel = settingsModel;
        this.mainFrame = mainFrame;
        this.settingsView.getApiKeyField().setText(this.settingsModel.apikey);
        this.settingsView.getIntervalSlider().setValue(this.settingsModel.getInterval());
        this.settingsView.getEnableNotifications().setSelected(this.settingsModel.isNotificationsEnabled());
        this.settingsView.getIntervalSlider().addChangeListener(e -> {
            int newValue = this.settingsView.getIntervalSlider().getValue();
            if(newValue != this.settingsModel.getInterval()){
                MonitoringDemonNotifier.getInstance().notifyIntervalUpdateListener(SettingsModel.cronValues[newValue]);
                AppInfoHandler.updateAppInfo(AppInfoKey.INTERVAL.name, newValue + "");
            }
        });
        this.settingsView.getEnableNotifications().addActionListener(e -> {
            boolean isSelected = this.settingsView.getEnableNotifications().isSelected();
            if(isSelected != this.settingsModel.isNotificationsEnabled()){
                AppInfoHandler.updateAppInfo(AppInfoKey.NOTIFICATIONS.name, isSelected ? "1" : "0");
            }
        });
        this.settingsView.getBackButton().addActionListener(e -> {
            this.switchBackToMainView();
        });
        this.settingsView.getSaveApiKeyButton().addActionListener(e -> {
            this.saveApiKey(new String(this.settingsView.getApiKeyField().getPassword()));
        });

    }

    private void saveApiKey(String apiKey) {
        if (apiKey.isBlank()) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "API key cannot be empty.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        AppInfoHandler.updateAppInfo(AppInfoKey.API_KEY.name, apiKey);
        logger.info("API KEY changed by user");
        JOptionPane.showMessageDialog(
                mainFrame,
                "API key changed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

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
