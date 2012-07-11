package com.nicstrong;

import android.accounts.*;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.extensions.android3.json.AndroidJsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android2.auth.GoogleAccountManager;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.GoogleKeyInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequest;
import com.spritemobile.googledrivetest.R;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GoogleDriveTestActivity extends ListActivity {
    // Logging level for HTTP requests/responses.
    private static final Level LOGGING_LEVEL = Level.FINE;
    private static final String TAG = "GoogleDriveTest";

    static final String PREF_ACCOUNT_NAME = "accountName";
    static final String PREF_AUTH_TOKEN = "authToken";
    private static final int MENU_ACCOUNTS = 0;

    private static final int REQUEST_AUTHENTICATE = 0;

    private static final String AUTH_TOKEN_TYPE = "oauth2:https://www.googleapis.com/auth/drive";

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = new AndroidJsonFactory();

    Drive service;
    private GoogleCredential credential = new GoogleCredential();
    private String accountName;
    private GoogleAccountManager accountManager;
    private SharedPreferences settings;
    private boolean received401 = false;
    private Button listFilesButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listFilesButton = (Button) findViewById(R.id.list_files);
        listFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncLoadFiles(GoogleDriveTestActivity.this).execute();
            }
        });
        listFilesButton.setEnabled(false);


        service = new Drive.Builder(transport, jsonFactory, credential)
                .setApplicationName(getPackageName())
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
        settings = getPreferences(MODE_PRIVATE);
        accountName = settings.getString(PREF_ACCOUNT_NAME, null);
        credential.setAccessToken(settings.getString(PREF_AUTH_TOKEN, null));
        Logger.getLogger("com.google.api.client").setLevel(LOGGING_LEVEL);
        accountManager = new GoogleAccountManager(this);
        checkAccount();
    }

    void checkAccount() {
        Account account = accountManager.getAccountByName(accountName);
        if (account == null) {
            chooseAccount();
            return;
        }
        if (credential.getAccessToken() != null) {
            onAuthToken();
            return;
        }
        Log.i(TAG, "Already using account " + accountName + " requesting token");
        accountManager.getAccountManager()
                .getAuthToken(account, AUTH_TOKEN_TYPE, true, new AccountManagerCallback<Bundle>() {

                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();
                            Log.i(TAG, "Auth token request complete");
                            if (bundle.containsKey(AccountManager.KEY_INTENT)) {
                                Intent intent = bundle.getParcelable(AccountManager.KEY_INTENT);
                                intent.setFlags(intent.getFlags() & ~Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivityForResult(intent, REQUEST_AUTHENTICATE);
                            } else if (bundle.containsKey(AccountManager.KEY_AUTHTOKEN)) {
                                setAuthToken(bundle.getString(AccountManager.KEY_AUTHTOKEN));
                                onAuthToken();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                }, null);
    }

    private void chooseAccount() {
        accountManager.getAccountManager().getAuthTokenByFeatures(GoogleAccountManager.ACCOUNT_TYPE,
                AUTH_TOKEN_TYPE,
                null,
                GoogleDriveTestActivity.this,
                null,
                null,
                new AccountManagerCallback<Bundle>() {

                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bundle;
                        try {
                            bundle = future.getResult();
                            Log.i(TAG, "Account auth complete");
                            setAccountName(bundle.getString(AccountManager.KEY_ACCOUNT_NAME));
                            setAuthToken(bundle.getString(AccountManager.KEY_AUTHTOKEN));
                            onAuthToken();
                        } catch (OperationCanceledException e) {
                            // user canceled
                        } catch (AuthenticatorException e) {
                            Log.e(TAG, e.getMessage(), e);
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e);
                        }
                    }
                },
                null);
    }

    void onAuthToken() {
        Toast.makeText(this, "Received the Auth Token", Toast.LENGTH_SHORT).show();
        listFilesButton.setEnabled(true);
    }

    void setAccountName(String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_ACCOUNT_NAME, accountName);
        editor.commit();
        this.accountName = accountName;
        Log.i(TAG, "Set accountName = " + accountName);
    }

    void setAuthToken(String authToken) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREF_AUTH_TOKEN, authToken);
        editor.commit();
        credential.setAccessToken(authToken);
        Log.i(TAG, "Set accessToken = " + authToken);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_AUTHENTICATE:
                if (resultCode == RESULT_OK) {
                    checkAccount();
                } else {
                    chooseAccount();
                }
                break;
        }
    }

    void handleGoogleException(IOException e) {
        if (e instanceof GoogleJsonResponseException) {
            GoogleJsonResponseException exception = (GoogleJsonResponseException) e;
            if (exception.getStatusCode() == 401 && !received401) {
                received401 = true;
                accountManager.invalidateAuthToken(credential.getAccessToken());
                credential.setAccessToken(null);
                SharedPreferences.Editor editor2 = settings.edit();
                editor2.remove(PREF_AUTH_TOKEN);
                editor2.commit();
                checkAccount();
                return;
            }
        }
        Log.e(TAG, e.getMessage(), e);
    }

    void onRequestCompleted() {
        received401 = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (accountManager.getAccounts().length >= 2) {
            menu.add(0, MENU_ACCOUNTS, 0, "Switch Account");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ACCOUNTS:
                chooseAccount();
                return true;
        }
        return false;
    }
}

