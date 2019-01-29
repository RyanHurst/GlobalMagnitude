package ryanhurst.globalmagnitude.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Score model
 *
 * Created by Ryan on 11/6/2017.
 */
@Entity
public class Score {

    @PrimaryKey(autoGenerate = true)
    public int scoreId;

    @ColumnInfo(name = "user_id")
    public String userId;

    @ColumnInfo(name = "score")
    public double score;

    @ColumnInfo(name = "match_time")
    public long matchTime;

    public Score(String userId, double score, long matchTime) {
        this.userId = userId;
        this.score = score;
        this.matchTime = matchTime;
    }
}
