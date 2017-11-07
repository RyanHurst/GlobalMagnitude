package ryanhurst.globalmagnitude;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Retrofit rest interface
 *
 * Created by Ryan on 11/6/2017.
 */

public interface GmRestService {

    @GET("leaderboard")
    Call<ArrayList<Leaderboard>> getLeaderboard();

    @POST("leaderboard")
    Call<Leaderboard> submitResults(@Body Leaderboard leaderboard);

}
