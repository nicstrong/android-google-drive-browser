package com.nicstrong.drive;

import android.accounts.Account;
import android.content.SharedPreferences;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;

import java.util.List;

public class DriveAccountManager {
	private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/drive";

	private static final String PREF_CURRENT_ACCOUNT = "current_account";
	private static final String PREF_AUTH_TOKEN = "auth_token_";

	private final GoogleAccountManager accountManager;
	private DriveAccount currentAccount;

	@Inject
	Provider<SharedPreferences> sharedPreferencesProvider;

	@Inject
	public DriveAccountManager(GoogleAccountManager accountManager) {
		this.accountManager = Preconditions.checkNotNull(accountManager);
	}

	public List<DriveAccount> getAccounts() {
		List<DriveAccount> accounts = Lists.newArrayList();
        SharedPreferences prefs = sharedPreferencesProvider.get();
        String currentAccountName = prefs.getString(PREF_CURRENT_ACCOUNT, null);

        for (Account account: accountManager.getAccounts()) {
			DriveAccount acc = new DriveAccount(account);
			if (currentAccount == null) {
                currentAccount = acc;
			}
            if (account.name.equals(currentAccountName)) {
                currentAccount = acc;
            }
			accounts.add(acc);
		}

        // Current account will either be first account or previously selected account.
        // Try get an AuthToken
        currentAccount.fetchAuthToken(accountManager, AUTH_TOKEN_TYPE);

		return accounts;
	}

    public DriveAccount getCurrentAccount() {
        return currentAccount;
    }
}
