package com.nicstrong.drive;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import javax.inject.Inject;

public class DriveService {
    public static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    public static final String AUTH_TOKEN_TYPE = "oauth2:" + DriveScopes.DRIVE;

    private final Drive drive;

    @Inject
    public DriveService(HttpTransport transport, JsonFactory jsonFactory, CredentialProvider credentialProvider, DriveBuilder builder) {
        Drive.Builder driveBuilder = new Drive.Builder(transport, jsonFactory, credentialProvider.get());
        builder.build(driveBuilder);
        drive = driveBuilder.build();
    }

}
