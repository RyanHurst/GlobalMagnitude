package ryanhurst.globalmagnitude;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
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
            return call.execute().body();
        } catch(IOException e) {
            Log.e(TAG, "error getting leaderboard");
            return null;
        }
    }

}
