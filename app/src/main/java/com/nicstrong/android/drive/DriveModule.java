package com.nicstrong.android.drive;

import android.content.Context;
import android.content.res.Resources;
import com.google.api.client.extensions.android.AndroidUtils;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.nicstrong.googledrivebrowser.R;

import javax.inject.Singleton;

public class DriveModule extends AbstractModule {
    @Override
    protected void configure() {
        this.bind(DriveAccountManager.class).in(Singleton.class);
        this.bind(CredentialProvider.class).to(DriveAccountManager.class);

        // DriveService dependencies
        bind(DriveService.class);
        bind(HttpTransport.class).toInstance(AndroidHttp.newCompatibleTransport());
        // AndroidJsonFactory only available in Honeycomb and above
        if (AndroidUtils.isMinimumSdkLevel(11)) {
            bind(JsonFactory.class).to(AndroidJsonFactory.class).in(Singleton.class);
        } else {
            bind(JsonFactory.class).to(GsonFactory.class).in(Singleton.class);
        }
    }

    @Provides
    DriveBuilder providesDriveBuilder(Context context, ApiKeyProvider apiKeyProvider, CredentialProvider credentialProvider) {
        return new AndroidAppDriveBuilder(apiKeyProvider, context.getPackageName(), credentialProvider);
    }

    /**
     * To prevent accidentally leaking my API key to Github I added the resource file with the
     * API key to the .gitignore. This therefore won't compile without changing this function.
     */
    @Provides
    ApiKeyProvider provideApiKeyProvider(final Resources resources) {
        return new ApiKeyProvider() {
            @Override
            public String getApiKey() {
                return resources.getString(R.string.drive_client_id);
            }
        };
    }

}
