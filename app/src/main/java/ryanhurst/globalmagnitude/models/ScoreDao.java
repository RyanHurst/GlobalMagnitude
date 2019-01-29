package ryanhurst.globalmagnitude.models;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ScoreDao {
    @Query("SELECT * FROM score ORDER BY score DESC, match_time ASC")
    LiveData<List<Score>> getAll();

    @Insert
    void insertAll(Score... scores);
}
