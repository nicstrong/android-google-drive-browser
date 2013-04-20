package com.nicstrong;


import com.google.inject.AbstractModule;
import com.nicstrong.drive.DriveAccountManager;

public class AndroidGoogleDriveBrowserModule extends AbstractModule {
	@Override
	protected void configure() {

        this.bind(DriveAccountManager.class);

	}
}
