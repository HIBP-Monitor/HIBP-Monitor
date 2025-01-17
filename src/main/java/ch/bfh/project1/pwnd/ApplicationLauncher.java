package ch.bfh.project1.pwnd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.SchedulerException;

import javax.swing.*;
import java.io.File;

public class ApplicationLauncher {
    private static final Logger logger = LogManager.getLogger(ApplicationLauncher.class);

    public static void main(String[] args) {
        try {
            MonitoringDemon monitoringDemon = new MonitoringDemon();
            monitoringDemon.initialize();
        } catch (SchedulerException | UnsupportedLookAndFeelException e) {
            logger.error("Failed to start the application: {}", e.getMessage(), e);
        }
    }
}

