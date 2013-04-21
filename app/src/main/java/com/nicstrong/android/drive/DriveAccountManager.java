package com.nicstrong.android.drive;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.SharedPreferences;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;

public class DriveAccountManager {
    private static final String PREF_CURRENT_ACCOUNT = "current_account";


    private DriveAccount currentAccount;

    private Provider<SharedPreferences> sharedPreferencesProvider;
    private Provider<AccountManager> accountManagerProvider;

    @Inject
    public DriveAccountManager(Provider<SharedPreferences> sharedPreferencesProvider,
                               Provider<AccountManager> accountManagerProvider) {
        this.sharedPreferencesProvider = Preconditions.checkNotNull(sharedPreferencesProvider);
        this.accountManagerProvider = Preconditions.checkNotNull(accountManagerProvider);
    }

    public List<DriveAccount> getAccounts() {

        SharedPreferences prefs = sharedPreferencesProvider.get();
        String currentAccountName = prefs.getString(PREF_CURRENT_ACCOUNT, null);

        AccountManager accountManager = accountManagerProvider.get();
        Account[] names = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        List<DriveAccount> accounts = Lists.newArrayList();
        for (int i = 0; i < names.length; i++) {
            accounts.add(new DriveAccount(names[i].name));
        }

        return accounts;
    }

    public DriveAccount getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(DriveAccount account) {
        currentAccount = account;
    }
}
