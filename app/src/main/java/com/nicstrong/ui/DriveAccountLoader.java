package com.nicstrong.ui;

import android.accounts.Account;
import android.content.Context;
import android.support.v4.content.Loader;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;
import com.nicstrong.drive.DriveAccount;
import com.nicstrong.drive.DriveAccountManager;
import com.nicstrong.util.InjectableAsyncLoader;

import java.util.List;

public class DriveAccountLoader extends InjectableAsyncLoader<List<DriveAccount>> {
	private final DriveAccountManager accountManager;

	public DriveAccountLoader(Context context, DriveAccountManager accountManager) {
		super(context);
		this.accountManager = accountManager;
	}

	@Override
	public List<DriveAccount> load() {
		return accountManager.getAccounts();
	}
}
