package com.nicstrong.android.drive;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.drive.DriveScopes;

public class DriveAccount implements CredentialProvider {
    public static final String DRIVE_SCOPE = "oauth2:" + DriveScopes.DRIVE;

    private String name;
    private Credential credential;

	public DriveAccount(String name) {
		this.name = name;
        this.credential = new GoogleCredential();
	}

    public boolean hasAccessToken() {
        return credential.getAccessToken() != null;
    }

	public void setAccessToken(String accessToken) {
        credential.setAccessToken(accessToken);
	}

    public String getName() {
        return name;
    }

	@Override
	public String toString() {
		return name;
	}

    @Override
    public Credential get() {
        return credential;
    }

    public String getAccessToken() {
        return credential.getAccessToken();
    }

    public String getScope() {
        return DRIVE_SCOPE;
    }
}
