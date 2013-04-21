package com.nicstrong.android.drive;

import android.content.res.Resources;
import com.google.inject.AbstractModule;
import com.nicstrong.googledrivebrowser.R;

public class DriveModule extends AbstractModule {
    @Override
    protected void configure() {
        this.bind(DriveAccountManager.class);
    }

    /**
     * To prevent accidentally leaking my API key to Github I added the resource file with the
     * API key to the .gitignore. This therefore won't compile without changing this function.
     */
    public ApiKeyProvider provideApiKeyProvider(final Resources resources) {
        return new ApiKeyProvider() {
            @Override
            public String getApiKey() {
                return resources.getString(R.string.google_api_key);
            }
        };
    }

}
