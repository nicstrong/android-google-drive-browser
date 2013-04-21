package com.nicstrong.android;


import com.google.inject.AbstractModule;
import com.nicstrong.android.drive.DriveModule;

public class AndroidGoogleDriveBrowserModule extends AbstractModule {
	@Override
	protected void configure() {
        install(new DriveModule());
	}
}
