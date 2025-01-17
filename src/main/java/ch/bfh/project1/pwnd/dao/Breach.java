package ch.bfh.project1.pwnd.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Breach {
    private String breachName;
    private String title;
    private String domain;
    private String breachDate;
    private String addedDate;
    private String modifiedDate;
    private int pwnCount;
    private String description;
    private String logoPath;
    private List<String> dataClasses;
    private boolean isVerified;
    private boolean isFabricated;
    private boolean isSensitive;
    private boolean isRetired;
    private boolean isSpamList;
    private boolean isMalware;
    private boolean isSubscriptionFree;

    public Breach() {}
    public Breach(ResultSet rs) throws SQLException {
        breachName = rs.getString("breachName");
        title = rs.getString("title");
        domain = rs.getString("domain");
        breachDate = rs.getString("breachDate");
        addedDate = rs.getString("addedDate");
        modifiedDate = rs.getString("modifiedDate");
        pwnCount = rs.getInt("pwnCount");
        description = rs.getString("description");
        logoPath = rs.getString("logoPath");
        isVerified = rs.getBoolean("isVerified");
        isFabricated = rs.getBoolean("isFabricated");
        isSensitive = rs.getBoolean("isSensitive");
        isRetired = rs.getBoolean("isRetired");
        isSpamList = rs.getBoolean("isSpamList");
        isMalware = rs.getBoolean("isMalware");
        isSubscriptionFree = rs.getBoolean("isSubscriptionFree");
    }

    // Getters and Setters
    public String getBreachName() {
        return breachName;
    }

    @JsonProperty("Name")
    public void setBreachName(String breachName) {
        this.breachName = breachName;
    }

    public String getTitle() {
        return title;
    }

    @JsonProperty("Title")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDomain() {
        return domain;
    }

    @JsonProperty("Domain")
    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getBreachDate() {
        return breachDate;
    }

    @JsonProperty("BreachDate")
    public void setBreachDate(String breachDate) {
        this.breachDate = breachDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    @JsonProperty("AddedDate")
    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    @JsonProperty("ModifiedDate")
    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getPwnCount() {
        return pwnCount;
    }

    @JsonProperty("PwnCount")
    public void setPwnCount(int pwnCount) {
        this.pwnCount = pwnCount;
    }

    public String getDescription() {
        return description;
    }

    @JsonProperty("Description")
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoPath() {
        return logoPath;
    }

    @JsonProperty("LogoPath")
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public List<String> getDataClasses() {
        return dataClasses;
    }

    @JsonProperty("DataClasses")
    public void setDataClasses(List<String> dataClasses) {
        this.dataClasses = dataClasses;
    }

    public boolean isVerified() {
        return isVerified;
    }

    @JsonProperty("IsVerified")
    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isFabricated() {
        return isFabricated;
    }

    @JsonProperty("IsFabricated")
    public void setFabricated(boolean fabricated) {
        isFabricated = fabricated;
    }

    public boolean isSensitive() {
        return isSensitive;
    }

    @JsonProperty("IsSensitive")
    public void setSensitive(boolean sensitive) {
        isSensitive = sensitive;
    }

    public boolean isRetired() {
        return isRetired;
    }

    @JsonProperty("IsRetired")
    public void setRetired(boolean retired) {
        isRetired = retired;
    }

    public boolean isSpamList() {
        return isSpamList;
    }

    @JsonProperty("IsSpamList")
    public void setSpamList(boolean spamList) {
        isSpamList = spamList;
    }

    public boolean isMalware() {
        return isMalware;
    }

    @JsonProperty("IsMalware")
    public void setMalware(boolean malware) {
        isMalware = malware;
    }

    public boolean isSubscriptionFree() {
        return isSubscriptionFree;
    }

    @JsonProperty("IsSubscriptionFree")
    public void setSubscriptionFree(boolean subscriptionFree) {
        isSubscriptionFree = subscriptionFree;
    }

    @Override
    public String toString() {
        return "Breach{" +
                "breachName='" + breachName + '\'' +
                ", title='" + title + '\'' +
                ", domain='" + domain + '\'' +
                ", breachDate='" + breachDate + '\'' +
                ", addedDate='" + addedDate + '\'' +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", pwnCount=" + pwnCount +
                ", description='" + description + '\'' +
                ", logoPath='" + logoPath + '\'' +
                ", dataClasses=" + dataClasses +
                ", isVerified=" + isVerified +
                ", isFabricated=" + isFabricated +
                ", isSensitive=" + isSensitive +
                ", isRetired=" + isRetired +
                ", isSpamList=" + isSpamList +
                ", isMalware=" + isMalware +
                ", isSubscriptionFree=" + isSubscriptionFree +
                '}';
    }
}