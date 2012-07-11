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
		DriveAccount first = null;
		for (Account account: accountManager.getAccounts()) {
			DriveAccount acc = new DriveAccount(account);
			if (first == null) {
				first = acc;
			}
			accounts.add(acc);
		}

//		SharedPreferences prefs = sharedPreferencesProvider.get();
//
//		if (prefs.contains(PREF_CURRENT_ACCOUNT)) {
//			if ()
//		}

		return accounts;
	}
}
