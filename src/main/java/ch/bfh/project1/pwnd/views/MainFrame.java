package ch.bfh.project1.pwnd.views;

import ch.bfh.project1.pwnd.MonitoringDemon;
import ch.bfh.project1.pwnd.controller.MainController;
import ch.bfh.project1.pwnd.db.AppInfoHandler;
import ch.bfh.project1.pwnd.enums.AppInfoKey;
import ch.bfh.project1.pwnd.listener.BreachUpdateListener;
import ch.bfh.project1.pwnd.listener.MonitoringDemonNotifier;
import ch.bfh.project1.pwnd.model.MainModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.net.URL;

public class MainFrame extends JFrame implements BreachUpdateListener {
    private SystemTray systemTray;
    private TrayIcon trayIcon;
    private boolean notificationActive = false; // Tracks if a notification icon is active
    private static final Logger logger = LogManager.getLogger(MainFrame.class);

    public MainFrame() {
        super("HIBPWND Monitor");
        MonitoringDemonNotifier.getInstance().addBreachUpdateListener(this);
        setApplicationIcon();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));

        setLocationRelativeTo(null);

        MainModel mainModel = new MainModel();
        MainView mainView = new MainView();
        MainController mainController = new MainController(mainModel, mainView, this);
        getContentPane().add(mainView);

        if (SystemTray.isSupported()) {
            initializeSystemTray();
        } else {
            logger.warn("SystemTray is not supported");
        }

        addWindowStateListener(createWindowStateListener());
        setVisible(true);
    }

    private void setApplicationIcon() {
        try {
            URL iconUrl = getClass().getResource("/icons/icon.png");
            if (iconUrl == null) {
                logger.error("iconUrl is null");
                throw new IOException("Icon resource not found!");
            }
            Image icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
            setIconImage(icon);
        } catch (IOException e) {
            logger.error("Failed to load icon", e);
        }
    }

    private void initializeSystemTray() {
        try {
            systemTray = SystemTray.getSystemTray();

            URL iconUrl = getClass().getResource("/icons/icon.png");
            if (iconUrl == null){
                logger.error("iconUrl is null");
                throw new IOException("Tray icon resource not found!");
            }
            Image trayImage = Toolkit.getDefaultToolkit().getImage(iconUrl);

            PopupMenu popupMenu = new PopupMenu();
            MenuItem openItem = new MenuItem("Open");
            MenuItem exitItem = new MenuItem("Exit");

            openItem.addActionListener(e -> {
                setVisible(true);
                setState(JFrame.NORMAL);
            });
            exitItem.addActionListener(e -> {
                if (trayIcon != null) {
                    systemTray.remove(trayIcon);
                }
                System.exit(0);
            });

            popupMenu.add(openItem);
            popupMenu.add(exitItem);

            trayIcon = new TrayIcon(trayImage, "HIBPWND Monitor", popupMenu);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(e -> {
                setVisible(true);
                setState(JFrame.NORMAL);
            });
        } catch (IOException e) {
            logger.error("Failed to initialize system tray", e);
        }
    }

    private WindowStateListener createWindowStateListener() {
        return new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (e.getNewState() == JFrame.ICONIFIED && systemTray != null && trayIcon != null) {
                    try {
                        systemTray.add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        logger.error("Failed to add tray icon!", ex);
                    }
                } else if (e.getNewState() == JFrame.NORMAL && systemTray != null && trayIcon != null) {
                    systemTray.remove(trayIcon);
                    try {
                        URL iconUrl = getClass().getResource("/icons/icon.png");
                        if (iconUrl != null) {
                            Image trayImage = Toolkit.getDefaultToolkit().getImage(iconUrl);
                            trayIcon.setImage(trayImage);
                            notificationActive = false;
                        }
                    } catch (Exception ee) {
                        logger.error(ee.getMessage(), ee);
                    }
                }
            }
        };
    }

    @Override
    public void onBreachUpdate(String message) {
        if (trayIcon != null) {
            if(AppInfoHandler.getAppInfoValueInt(AppInfoKey.NOTIFICATIONS.name) == 1) {
                trayIcon.displayMessage("Breach Alert", message, TrayIcon.MessageType.INFO);
            }
            if (!notificationActive) {
                try {
                    URL iconUrl = getClass().getResource("/icons/iconNotification.png");
                    if (iconUrl != null) {
                        Image trayImage = Toolkit.getDefaultToolkit().getImage(iconUrl);
                        trayIcon.setImage(trayImage);
                        notificationActive = true;
                    }
                } catch (Exception e) {
                    logger.error("Cant set notification icon", e);
                }
            }
        }
    }
}
