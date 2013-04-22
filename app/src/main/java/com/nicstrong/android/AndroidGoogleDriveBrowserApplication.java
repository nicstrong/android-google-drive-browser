package com.nicstrong.android;

import android.app.Application;
import com.nicstrong.android.util.LogUtils;

import java.util.logging.Level;

public class AndroidGoogleDriveBrowserApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.setupLogging(null, Level.ALL); // null tag will use JUL tags
        LogUtils.enableGoogleHttpClientLogging(Level.ALL);
    }
}
