package ryanhurst.globalmagnitude;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Locale;

import androidx.room.Room;
import ryanhurst.globalmagnitude.models.AppDatabase;
import ryanhurst.globalmagnitude.models.TriviaGame;

/**
 * Created by Ryan on 11/8/2017.
 */

public class GmHelper {

    private static final String USERNAME_KEY = "username";
    private static final String DATABASE_NAME = "GlobalMagnitude";

    public static String formatElapsedTime(long elapsedTimeMillis) {
        long elapsedMinutes = elapsedTimeMillis / (1000 * 60);
        long leftOverMillis = elapsedTimeMillis % (1000 * 60);
        long elapsedSeconds = leftOverMillis / 1000;
        leftOverMillis = leftOverMillis % 1000;
        long elapsedTenths = leftOverMillis / 100;

        return String.format(Locale.US, "%02d:%02d.%01d", elapsedMinutes, elapsedSeconds, elapsedTenths);
    }

    public static String getElapsedSeconds(long elapsedTimeMillis) {
        double elapsedSeconds = ((double) elapsedTimeMillis) / 1000;
        return String.format(Locale.US, "%.2f", elapsedSeconds);
    }

    public static String formatScore(double score) {
        double percent = score * 100;
        return String.format(Locale.US, "%.1f%s", percent, "%");
    }

    public static int getNumberCorrect(TriviaGame triviaGame) {
        int numberCorrect = 0;

        for(TriviaGame.Round r : triviaGame.rounds) {
            int userAnswer = r.answers.get(r.userAnswerIndex);
            if(r.factor1 * r.factor2 == userAnswer) {
                numberCorrect++;
            }
        }

        return numberCorrect;
    }

    public static AppDatabase getDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(USERNAME_KEY, "");
    }

    public static void setUsername(Context context, String username) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit()
                .putString(USERNAME_KEY, username)
                .apply();
    }
}
