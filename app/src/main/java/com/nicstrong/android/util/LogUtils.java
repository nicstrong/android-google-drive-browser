package com.nicstrong.android.util;

import com.google.api.client.http.HttpTransport;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtils {
	public static void setupLogging(String prefix, Level level) {
		Logger rootLogger = Logger.getLogger("");
		removeAllHandlers(rootLogger);
		LogCatHandler logCatHandler = new LogCatHandler(prefix);
		logCatHandler.setLevel(level);
		rootLogger.setLevel(level);
		rootLogger.addHandler(logCatHandler);
	}

	public static void removeAllHandlers(Logger logger) {
		Handler[] handlers = logger.getHandlers();
		for (int i = 0; i < handlers.length; ++i) {
			logger.removeHandler(handlers[i]);
		}
	}

	public static void enableHttpClientTrace(boolean director, boolean headers, boolean wire) {
		if (director) {
			Logger apacheRequestDirector = Logger.getLogger(org.apache.http.impl.client.DefaultRequestDirector.class.getName());
			apacheRequestDirector.setLevel(Level.FINEST);
		}
		if (headers) {
			Logger apacheHeaderLog = Logger.getLogger("org.apache.http.headers");
			apacheHeaderLog.setLevel(Level.FINEST);
		}
		if (wire) {
			Logger apacheWireLog = Logger.getLogger("org.apache.http.wire");
			apacheWireLog.setLevel(Level.FINEST);
		}
	}

    public static void enableGoogleHttpClientLogging(Level level) {
        Logger logger = Logger.getLogger(HttpTransport.class.getName());
        logger.setLevel(level);
    }

    private LogUtils() {
    }
}

