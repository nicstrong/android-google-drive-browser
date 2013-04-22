package com.nicstrong.android.drive;

import android.content.Context;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DriveService {
    private static final Logger logger = Logger.getLogger(DriveService.class.getName());

    public static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

    private final Drive drive;
    private final DriveAccountManager driveAccountManager;

    @Inject
    public DriveService(HttpTransport transport, JsonFactory jsonFactory, DriveAccountManager driveAccountManager,
                        DriveBuilder builder) {
        this.driveAccountManager = driveAccountManager;
        Drive.Builder driveBuilder = new Drive.Builder(transport, jsonFactory, driveAccountManager.get());
        builder.build(driveBuilder);
        drive = driveBuilder.build();
    }

    public List<File> findFiles(String queryString, String fields) throws IOException {
        List<File> result = new ArrayList<File>();
        Drive.Files.List request = null;
        try {
            request = drive.files().list();
            request.setQ(queryString);
            if (fields != null) {
                request.setFields(fields);
            }
            do {
                FileList files = request.execute();

                result.addAll(files.getItems());
                request.setPageToken(files.getNextPageToken());
            } while (request.getPageToken() != null && request.getPageToken().length() > 0);

        } catch (IOException e) {
            if (request != null) {
                request.setPageToken(null);
            }
            throw e;
        }
        return result;
    }


    public void refreshToken(Context context) throws IOException {
        Preconditions.checkNotNull(driveAccountManager.getCurrentAccount());

        DriveAccount currentAcc = driveAccountManager.getCurrentAccount();

        if (currentAcc.hasAccessToken()) {
            GoogleAuthUtil.invalidateToken(context, currentAcc.getAccessToken());
        }
        try {
            logger.fine("Refreshing auth token for " + currentAcc.getName() + " on scope " + currentAcc.getScope());
            currentAcc.setAccessToken(GoogleAuthUtil.getToken(context, currentAcc.getName(), currentAcc.getScope()));
        } catch (GoogleAuthException e) {
            throw new IllegalStateException("Should have google play services");
        }
    }
}
