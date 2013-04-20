package com.nicstrong;


import com.google.inject.AbstractModule;
import com.nicstrong.drive.DriveAccountManager;
import com.nicstrong.drive.DriveModule;

public class AndroidGoogleDriveBrowserModule extends AbstractModule {
	@Override
	protected void configure() {
        install(new DriveModule());
	}
}
