package ryanhurst.globalmagnitude;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ryanhurst.globalmagnitude.models.Score;

/**
 * Retrofit rest interface
 *
 * Created by Ryan on 11/6/2017.
 */

public interface GmRestService {

    @GET("leaderboard")
    Call<ArrayList<Score>> getLeaderboard();

    @POST("score")
    Call<Score> submitScore(@Body Score score);

}
