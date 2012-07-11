package com.nicstrong.drive;


import android.accounts.Account;

public class DriveAccount {
	private Account account;
	private String authToken;

	public DriveAccount(Account acc) {
		this.account = acc;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	@Override
	public String toString() {
		return account.name;
	}
}
