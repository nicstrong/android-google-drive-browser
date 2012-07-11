package com.nicstrong.drive;

import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.extensions.android3.json.AndroidJsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequest;
import com.nicstrong.GoogleApiKeys;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriveService {
	// Logging level for HTTP requests/responses.
	private static final Level LOGGING_LEVEL = Level.FINE;

	final HttpTransport transport = AndroidHttp.newCompatibleTransport();
	final JsonFactory jsonFactory = new AndroidJsonFactory();

	private GoogleCredential credential = new GoogleCredential();
	private Drive service;


	public DriveService() {
		service = new Drive.Builder(transport, jsonFactory, credential)
				.setApplicationName("AndroidGoogleDrive/1.0")
				.setJsonHttpRequestInitializer(new JsonHttpRequestInitializer() {
					@Override
					public void initialize(JsonHttpRequest jsonHttpRequest) throws IOException {
						DriveRequest driveRequest = (DriveRequest) jsonHttpRequest;
						driveRequest.setPrettyPrint(true);
						driveRequest.setKey(GoogleApiKeys.CLIENT_ID);
						driveRequest.setOauthToken(credential.getAccessToken());
					}
				})
				.build();


		Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);

	}

	//    void handleGoogleException(IOException e) {
//        if (e instanceof GoogleJsonResponseException) {
//            GoogleJsonResponseException exception = (GoogleJsonResponseException) e;
//            if (exception.getStatusCode() == 401 && !received401) {
//                received401 = true;
//                accountManager.invalidateAuthToken(credential.getAccessToken());
//                credential.setAccessToken(null);
//                SharedPreferences.Editor editor2 = settings.edit();
//                editor2.remove(PREF_AUTH_TOKEN);
//                editor2.commit();
//                checkAccount();
//                return;
//            }
//        }
//        Log.e(TAG, e.getMessage(), e);
//    }

}
