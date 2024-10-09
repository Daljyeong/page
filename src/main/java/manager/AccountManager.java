package manager;

import models.Account;


public interface AccountManager {

    public void addAccount(Account account);

    public Account getAccountById(String id);

    public void saveData();

    public void loadData();
}
