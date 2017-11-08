package ryanhurst.globalmagnitude.models;

import com.google.gson.annotations.SerializedName;

/**
 * Score model
 *
 * Created by Ryan on 11/6/2017.
 */

public class Score {
    @SerializedName("userid")
    public String userId;

    @SerializedName("score")
    public int score;

    @SerializedName("mtime")
    public double matchTime;

    @SerializedName("qtime")
    public double averageQuestionTime;
}
