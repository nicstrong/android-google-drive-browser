package com.nicstrong.android.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * JUL Handler that maps to the Android logging framework. The default
 * Android implementation of the JUL logger obeys the Android log level.
 * This implementation uses the JUL logging levels instead.
 */
public class LogCatHandler extends Handler {
	private static final int MAX_LOG_TAG_LENGTH = 23;
	public static final Formatter THE_FORMATTER = new Formatter() {
        @Override
        public String format(LogRecord r) {
            Throwable thrown = r.getThrown();
            if (thrown != null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                sw.write(r.getMessage());
                sw.write("\n");
                thrown.printStackTrace(pw);
                pw.flush();
                return sw.toString();
            } else {
                return r.getMessage();
            }
        }
    };

	public LogCatHandler(String prefix) {
		setFormatter(THE_FORMATTER);
    }

    @Override
    public void close() {
    }

    @Override
    public void flush() {
    }

    @Override
    public void publish(LogRecord record) {
        try {
            int level = getAndroidLevel(record.getLevel());
            String tag = makeLogTag(record.getLoggerName() == null ? "null" : record.getLoggerName());
            String message = getFormatter().format(record);
            Log.println(level, tag, message);
        } catch (RuntimeException e) {
            Log.e("LogCatHandler", "Error logging message.", e);
        }
    }

    static int getAndroidLevel(Level level) {
        int value = level.intValue();
        if (value >= 1000) { // SEVERE
            return Log.ERROR;
        } else if (value >= 900) { // WARNING
            return Log.WARN;
        } else if (value >= 800) { // INFO
            return Log.INFO;
        } else {
            return Log.DEBUG;
        }
    }

	String makeLogTag(String str) {
		if (str.length() > MAX_LOG_TAG_LENGTH) {
			// Most loggers use the full class name. Try dropping the package.
			int lastPeriod = str.lastIndexOf(".");
			if (lastPeriod > 0) {
				str = str.substring(lastPeriod + 1);
			}
			if (str.length() > MAX_LOG_TAG_LENGTH) {
				return str.substring(0, MAX_LOG_TAG_LENGTH - 1);
			}
		}
		return str;
	}

}
