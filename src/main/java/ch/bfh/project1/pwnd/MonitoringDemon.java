package ch.bfh.project1.pwnd;

import ch.bfh.project1.pwnd.db.AppInfoHandler;
import ch.bfh.project1.pwnd.db.DatabaseHandler;
import ch.bfh.project1.pwnd.enums.AppInfoKey;
import ch.bfh.project1.pwnd.jobs.CronJob;
import ch.bfh.project1.pwnd.listener.IntervalUpdateListener;
import ch.bfh.project1.pwnd.listener.MonitoringDemonNotifier;
import ch.bfh.project1.pwnd.listener.NewAccountListener;
import ch.bfh.project1.pwnd.model.SettingsModel;
import ch.bfh.project1.pwnd.views.MainFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.swing.*;

public class MonitoringDemon implements IntervalUpdateListener, NewAccountListener {
    private static final Logger logger = LogManager.getLogger(MonitoringDemon.class);
    private Scheduler scheduler;
    private JobKey jobKey;


    public MonitoringDemon() {
        MonitoringDemonNotifier.getInstance().addIntervalListener(this);
        MonitoringDemonNotifier.getInstance().addNewAccountListener(this);
    }

    public void initialize() throws SchedulerException, UnsupportedLookAndFeelException {
        logger.info("Start application");
        DatabaseHandler.setUpDB();
        checkAndPromptForApiKey();
        startScheduler();
        UIManager.setLookAndFeel(new FlatDarkLaf());
        SwingUtilities.invokeLater(MainFrame::new);
    }

    private void startScheduler() throws SchedulerException {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDetail job = JobBuilder.newJob(CronJob.class)
                .withIdentity("cronJob", "group1")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("cronTrigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule(
                        SettingsModel.cronValues[AppInfoHandler.getAppInfoValueInt(AppInfoKey.INTERVAL.name)]
                ))
                .build();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
        this.jobKey = job.getKey();
        scheduler.triggerJob(job.getKey()); // Trigger immediately
    }

    private void checkAndPromptForApiKey() {
        String apiKey = AppInfoHandler.getAppInfoValueString(AppInfoKey.API_KEY.name); // Retrieve the API key

        if (apiKey == null || apiKey.isBlank()) {
            apiKey = promptForApiKey();
            if (apiKey == null || apiKey.isBlank()) {
                JOptionPane.showMessageDialog(null, "API Key is required. Exiting the application.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
            AppInfoHandler.updateAppInfo(AppInfoKey.API_KEY.name, apiKey);
        }
    }

    private String promptForApiKey() {
        return JOptionPane.showInputDialog(
                null,
                "Please enter your API Key:",
                "API Key Required",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    @Override
    public void onIntervalChange(String interval) {
        TriggerKey triggerKey = new TriggerKey("cronTrigger", "group1");
        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(CronScheduleBuilder.cronSchedule(interval))
                .build();
        try {
            scheduler.rescheduleJob(triggerKey, newTrigger);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onNewAccount() {
        try {
            scheduler.triggerJob(this.jobKey);
        } catch (SchedulerException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

