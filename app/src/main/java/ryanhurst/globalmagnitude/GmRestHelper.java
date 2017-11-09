package ryanhurst.globalmagnitude;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ryanhurst.globalmagnitude.models.Score;

/**
 * Retrofit rest helper
 *
 * Created by Ryan on 11/6/2017.
 */

public class GmRestHelper {

    private static final String TAG = "GmRestHelper";

    private static GmRestService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://misc.tealdrones.com/global-magnitude/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GmRestService.class);
    }


    static ArrayList<Score> getLeaderboard() {
        Call<ArrayList<Score>> call = getService().getLeaderboard();
        try {
            Response<ArrayList<Score>> response = call.execute();
            if(response.isSuccessful()) {
                Log.d(TAG, "got leaderboard");
                return response.body();
            } else {
                Log.e(TAG, "error getting leaderboard: " + response.message());
                return null;
            }
        } catch(IOException e) {
            Log.e(TAG, "error getting leaderboard", e);
            return null;
        }
    }

    static boolean submitScore(Score score) {
        Call<Void> call = getService().submitScore(score);
        try {
            Response<Void> response = call.execute();
            if(response.isSuccessful()) {
                Log.d(TAG, "submitted score");
                return true;
            } else {
                Log.e(TAG, "error submitting score: " + response.message());
                return false;
            }
        } catch(IOException e) {
            Log.e(TAG, "error submitting score", e);
            return false;
        }
    }

}
