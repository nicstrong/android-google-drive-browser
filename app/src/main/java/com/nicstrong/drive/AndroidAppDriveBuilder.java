package com.nicstrong.drive;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.googleapis.services.json.CommonGoogleJsonClientRequestInitializer;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequest;

import java.io.IOException;

public class AndroidAppDriveBuilder implements DriveBuilder {
    private final String apiKey;
    private final String packageName;
    private final CredentialProvider credentialProvider;

    public AndroidAppDriveBuilder(String apiKey, String packageName, CredentialProvider credentialProvider) {
        this.apiKey = apiKey;
        this.packageName = packageName;
        this.credentialProvider = credentialProvider;
    }
    @Override
    public void build(Drive.Builder builder) {

        builder.setApplicationName(packageName);
        builder.setGoogleClientRequestInitializer(new CommonGoogleJsonClientRequestInitializer() {
            @Override
            protected void initializeJsonRequest(AbstractGoogleJsonClientRequest<?> request) throws IOException {
                DriveRequest driveRequest = (DriveRequest) request;
                driveRequest.setPrettyPrint(true);
                driveRequest.setKey(apiKey);
                driveRequest.setOauthToken(credentialProvider.get().getAccessToken());
            }
        });
    }
}
