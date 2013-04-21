package com.nicstrong.android.drive;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DriveService {
    public static final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

    private final Drive drive;

    @Inject
    public DriveService(HttpTransport transport, JsonFactory jsonFactory, CredentialProvider credentialProvider, DriveBuilder builder) {
        Drive.Builder driveBuilder = new Drive.Builder(transport, jsonFactory, credentialProvider.get());
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


}
