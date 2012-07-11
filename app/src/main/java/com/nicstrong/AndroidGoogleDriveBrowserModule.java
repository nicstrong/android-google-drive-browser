package com.nicstrong;


import android.content.Context;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.nicstrong.drive.DriveAccountManager;

public class AndroidGoogleDriveBrowserModule extends AbstractModule {
	@Override
	protected void configure() {

	}

	@Provides
	DriveAccountManager driveAccountManager(GoogleAccountManager accountManager) {
		return new DriveAccountManager(accountManager);
	}

	@Provides
	GoogleAccountManager googleAccountManager(Context context) {
		return new GoogleAccountManager(context);
	}

}
