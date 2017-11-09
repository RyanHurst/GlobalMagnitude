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
    public String score;

    @SerializedName("mtime")
    public String matchTime;

    @SerializedName("qtime")
    public String averageQuestionTime;

    public Score(String userId, String score, String matchTime, String averageQuestionTime) {
        this.userId = userId;
        this.score = score.replace("%", "");
        this.matchTime = matchTime;
        this.averageQuestionTime = averageQuestionTime;
    }
}
