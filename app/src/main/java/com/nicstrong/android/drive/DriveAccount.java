package com.nicstrong.android.drive;

import com.google.api.services.drive.DriveScopes;

public class DriveAccount {
    public static final String DRIVE_SCOPE = "oauth2:" + DriveScopes.DRIVE;

    private String name;
    private String accessToken;
    private DriveAccountManager driveAccountChangeListener;

    public DriveAccount(String name) {
		this.name = name;

    }

    public boolean hasAccessToken() {
        return accessToken != null;
    }

	public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        if (driveAccountChangeListener != null) {
            driveAccountChangeListener.onAccessToken(accessToken);
        }
	}

    public String getName() {
        return name;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getScope() {
        return DRIVE_SCOPE;
    }

    public void setDriveAccountChangeListener(DriveAccountManager driveAccountChangeListener) {
        this.driveAccountChangeListener = driveAccountChangeListener;
    }

    @Override
    public String toString() {
        return "DriveAccount{" +
                "name='" + name + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
