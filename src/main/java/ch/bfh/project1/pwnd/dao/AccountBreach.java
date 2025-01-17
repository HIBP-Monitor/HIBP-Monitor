package ch.bfh.project1.pwnd.dao;

public class AccountBreach {

    Account account;
    Breach breach;
    boolean handled;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Breach getBreach() {
        return breach;
    }

    public void setBreach(Breach breach) {
        this.breach = breach;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}
