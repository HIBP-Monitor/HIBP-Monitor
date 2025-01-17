package ch.bfh.project1.pwnd.model;

import ch.bfh.project1.pwnd.dao.Account;
import ch.bfh.project1.pwnd.dao.AccountBreach;
import ch.bfh.project1.pwnd.dao.Breach;
import ch.bfh.project1.pwnd.db.AccountBreachHandler;
import ch.bfh.project1.pwnd.db.AccountHandler;
import ch.bfh.project1.pwnd.db.AppInfoHandler;
import ch.bfh.project1.pwnd.enums.AppInfoKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainModel {

    public record MainTableEntry(Account account, Breach[] breaches, int unhandled) {}

    List<MainTableEntry> mainTableEntryList = new ArrayList<>();
    Map<String, List<AccountBreach>> accountBreachMap = new HashMap<>();
    List<Account> accountList = new ArrayList<>();
    String lastApiCall;
    public MainModel() {
        update();

    }

    public void update() {
        accountList = AccountHandler.findAllAccounts();
        mainTableEntryList.clear();
        for (Account account : accountList) {
            List<AccountBreach> accountBreachList = AccountBreachHandler.findAllBreachesByAccount(account.getEmail());
            accountBreachMap.put(account.getEmail(), accountBreachList);

            Breach[] breaches = accountBreachList.stream()
                    .map(AccountBreach::getBreach)
                    .toArray(Breach[]::new);

            int unhandledCount = (int) accountBreachList.stream()
                    .filter(accountBreach -> !accountBreach.isHandled())
                    .count();
            mainTableEntryList.add(new MainTableEntry(account, breaches, unhandledCount));
        }
        lastApiCall = AppInfoHandler.getAppInfoValueString(AppInfoKey.LAST_API_CALL.name);
    }

    public List<MainTableEntry> getMainTableEntryList() {
        return mainTableEntryList;
    }
    public List<Account> getAccountList() {
        return accountList;
    }

    public Map<String, List<AccountBreach>> getAccountBreachMap() {
        return accountBreachMap;
    }

    public String getLastApiCall() {
        return lastApiCall;
    }
}
