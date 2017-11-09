package ryanhurst.globalmagnitude;

import java.util.Locale;

/**
 * Created by Ryan on 11/8/2017.
 */

public class GmHelper {

    public static String formatElapsedTime(long elapsedTimeMillis) {
        long elapsedMinutes = elapsedTimeMillis / (1000 * 60);
        long leftOverMillis = elapsedTimeMillis % (1000 * 60);
        long elapsedSeconds = leftOverMillis / 1000;
        leftOverMillis = leftOverMillis % 1000;
        long elapsedHundredths = leftOverMillis / 10;

        return String.format(Locale.US, "%02d:%02d.%02d", elapsedMinutes, elapsedSeconds, elapsedHundredths);
    }

    public static String getElapsedSeconds(long elapsedTimeMillis) {
        double elapsedSeconds = ((double) elapsedTimeMillis) / 1000;
        return String.format(Locale.US, "%.2f", elapsedSeconds);
    }

}
