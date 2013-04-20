package com.nicstrong.drive;

import com.google.api.client.auth.oauth2.Credential;

public interface CredentialProvider {
    Credential get();
}
