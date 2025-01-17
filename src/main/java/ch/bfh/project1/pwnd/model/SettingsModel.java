package ch.bfh.project1.pwnd.model;

import ch.bfh.project1.pwnd.db.AppInfoHandler;
import ch.bfh.project1.pwnd.enums.AppInfoKey;

public class SettingsModel {

    private boolean notificationsEnabled;
    private int interval;
    public static final int[] allowedValues = {10, 30, 60, 120, 240};
    public static final String[] cronValues = {
            "0 0/10 * * * ?", "0 0/30 * * * ?",
            "0 0 * * * ?", "0 0 0/2 * * ?",
            "0 0 0/4 * * ?"};
    public String apikey;
    public SettingsModel(){
        update();
    }
    public void update(){
        notificationsEnabled = AppInfoHandler.getAppInfoValueInt(AppInfoKey.NOTIFICATIONS.name) == 1;
        interval = AppInfoHandler.getAppInfoValueInt(AppInfoKey.INTERVAL.name);
        apikey = AppInfoHandler.getAppInfoValueString(AppInfoKey.API_KEY.name);
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public int getInterval() {
        return interval;
    }
}
