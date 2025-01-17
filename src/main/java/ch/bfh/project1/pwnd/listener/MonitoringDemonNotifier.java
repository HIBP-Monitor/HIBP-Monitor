package ch.bfh.project1.pwnd.listener;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MonitoringDemonNotifier {
    private static MonitoringDemonNotifier instance;
    private final List<BreachUpdateListener> breachUpdateListeners = new ArrayList<>();
    private final List<IntervalUpdateListener> intervalUpdateListeners = new ArrayList<>();
    private final List<ApiCallListener> apiCallListeners = new ArrayList<>();
    private final List<NewAccountListener> newAccountListeners = new ArrayList<>();

    public static MonitoringDemonNotifier getInstance() {
        if (instance == null) {
            instance = new MonitoringDemonNotifier();
        }
        return instance;
    }

    public void addBreachUpdateListener(BreachUpdateListener listener) {
        breachUpdateListeners.add(listener);
    }

    public void addIntervalListener(IntervalUpdateListener listener) {
        intervalUpdateListeners.add(listener);
    }

    public void addApiCallListener(ApiCallListener listener) {
        apiCallListeners.add(listener);
    }

    public void addNewAccountListener(NewAccountListener listener) {
        newAccountListeners.add(listener);
    }

    public void removeListener(BreachUpdateListener listener) {
        breachUpdateListeners.remove(listener);
    }

    public void removeListener(IntervalUpdateListener listener) {
        intervalUpdateListeners.remove(listener);
    }

    public void removeListener(ApiCallListener listener) {
        apiCallListeners.remove(listener);
    }

    public void removeNewAccountListener(NewAccountListener listener) {
        newAccountListeners.remove(listener);
    }

    public void notifyBreachUpdateListeners(String message) {
        for (BreachUpdateListener listener : breachUpdateListeners) {
            SwingUtilities.invokeLater(() -> listener.onBreachUpdate(message));
        }
    }

    public void notifyIntervalUpdateListener(String message) {
        for (IntervalUpdateListener listener : intervalUpdateListeners) {
            SwingUtilities.invokeLater(() -> listener.onIntervalChange(message));
        }
    }

    public void notifyApiCallListener() {
        for (ApiCallListener listener : apiCallListeners) {
            SwingUtilities.invokeLater(listener::onApiCalled);
        }
    }

    public void notifyNewAccountListeners() {
        for (NewAccountListener listener : newAccountListeners) {
            SwingUtilities.invokeLater(listener::onNewAccount);
        }
    }
}
