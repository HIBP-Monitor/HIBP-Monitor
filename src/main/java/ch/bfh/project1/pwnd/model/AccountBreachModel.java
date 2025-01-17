package ch.bfh.project1.pwnd.model;

import ch.bfh.project1.pwnd.dao.Account;
import ch.bfh.project1.pwnd.dao.AccountBreach;

import java.util.List;


public class AccountBreachModel {

    private Account account;
    private List<AccountBreach> accountBreachList;


    public AccountBreachModel(Account account, List<AccountBreach> accountBreachList) {
        this.account = account;
        this.accountBreachList = accountBreachList;
        this.accountBreachList.sort((a, b) -> Boolean.compare(a.isHandled(), b.isHandled()));
    }
    public Account getAccount() {
        return account;
    }

    public List<AccountBreach> getAccountBreachList() {
        return accountBreachList;
    }
}
