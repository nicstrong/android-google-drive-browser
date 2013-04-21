package com.nicstrong.android.drive;

import com.google.api.client.auth.oauth2.Credential;

public interface CredentialProvider {
    Credential get();
}
