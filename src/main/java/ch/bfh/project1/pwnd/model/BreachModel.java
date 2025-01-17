package ch.bfh.project1.pwnd.model;

import ch.bfh.project1.pwnd.dao.Breach;
import ch.bfh.project1.pwnd.db.AccountBreachHandler;
import ch.bfh.project1.pwnd.db.BreachHandler;
import ch.bfh.project1.pwnd.db.BreachTypeHandler;

import java.util.List;

public class BreachModel {

    String breachName;
    Breach breach;
    List<String> breachTypes;
    List<String> affectedAccounts;

    public BreachModel(String breachName){
       this.breachName = breachName;
        this.update();
    }

    private void update() {
        breach = BreachHandler.findBreachByKey(breachName);
        breachTypes = BreachTypeHandler.findAllTypesByBreach(breachName);
        affectedAccounts = AccountBreachHandler.findAllAccountsAffectedByBreach(breachName);
    }

    public List<String> getAffectedAccounts() {
        return affectedAccounts;
    }

    public List<String> getBreachTypes() {
        return breachTypes;
    }

    public Breach getBreach() {
        return breach;
    }

    public String getBreachName() {
        return breachName;
    }
}
