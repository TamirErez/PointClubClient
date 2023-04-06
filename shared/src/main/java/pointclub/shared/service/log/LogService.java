package pointclub.shared.service.log;

import android.util.Log;

public class LogService {
    private static final String POINTCLUB_TAG = "pointclublog";

    public static void info(LogTag tag, String message) {
        Log.i(POINTCLUB_TAG + ": " + tag.name().toLowerCase(), message);
    }
    public static void warn(LogTag tag, Throwable exception) {
        Log.w(POINTCLUB_TAG + ": " + tag.name().toLowerCase(), exception);
    }
    public static void warn(LogTag tag, String message) {
        Log.w(POINTCLUB_TAG + ": " + tag.name().toLowerCase(), message);
    }

    public static void error(LogTag tag, String message) {
        Log.e(POINTCLUB_TAG + ": " + tag.name().toLowerCase(), message);
    }

    public static void error(LogTag tag, String message, Throwable exception) {
        Log.e(POINTCLUB_TAG + ": " + tag.name().toLowerCase(), message, exception);
    }
}
