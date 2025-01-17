package ch.bfh.project1.pwnd.enums;

public enum AppInfoKey {
    API_CALLS("API_CALLS"),
    NOTIFICATIONS("NOTIFICATIONS"),
    INTERVAL("API_INTERVAL"),
    API_KEY("API_KEY"),
    LAST_API_CALL("LAST_API_CALL"),;

    public final String name;
    AppInfoKey(String name) {
        this.name = name;
    }
}
