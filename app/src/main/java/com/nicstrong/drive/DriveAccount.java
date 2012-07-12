package com.nicstrong.drive;


import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.text.TextUtils;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriveAccount {
    private static final Logger logger = Logger.getLogger(DriveAccount.class.getName());
	private Account account;
	private String authToken;

	public DriveAccount(Account acc) {
		this.account = acc;
	}

    public boolean hasAuthToken() {
        return !TextUtils.isEmpty(authToken);
    }
	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

    public String getName() {
        return account.name;
    }

	@Override
	public String toString() {
		return account.name;
	}

    public void fetchAuthToken(GoogleAccountManager accountManager, String tokenType) {
        try {
            authToken = accountManager.getAccountManager().blockingGetAuthToken(account, tokenType, false);
        } catch (AuthenticatorException e) {
            logger.log(Level.WARNING, "Failed to fetch AuthToken", e);
        } catch (OperationCanceledException e) {
            logger.log(Level.WARNING, "Failed to fetch AuthToken", e);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to fetch AuthToken", e);
        }
    }
}
