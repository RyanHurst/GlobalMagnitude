package ryanhurst.globalmagnitude;

import com.google.gson.annotations.SerializedName;

/**
 * Leaderboard object
 *
 * Created by Ryan on 11/6/2017.
 */

public class Leaderboard {
    @SerializedName("userid")
    public String userId;

    @SerializedName("score")
    public String score;

    @SerializedName("mtime")
    public String matchTime;

    @SerializedName("qtime")
    public String averageQuestionTime;
}
