package ryanhurst.globalmagnitude.viewmodels;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ryanhurst.globalmagnitude.GetLeaderboardAsyncTask;
import ryanhurst.globalmagnitude.models.Score;

/**
 * ViewModel for leaderboard
 * <p>
 * Created by Ryan on 11/6/2017.
 */

public class LeaderboardViewModel extends ObservableViewModel {

    private GetLeaderboardAsyncTask getLeaderboardAsynctask;

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Score>> leaderboardLiveData = new MutableLiveData<>();

    public LiveData<ArrayList<Score>> getLeaderboard() {
        if((loading.getValue() == null || !loading.getValue()) &&
                (leaderboardLiveData == null || leaderboardLiveData.getValue() == null)) {
            getLeaderboardAsynctask = new GetLeaderboardAsyncTask(this);
            loading.setValue(true);
            getLeaderboardAsynctask.execute();
        }

        return leaderboardLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(getLeaderboardAsynctask != null) {
            getLeaderboardAsynctask.cancel(true);
            getLeaderboardAsynctask = null;
        }
    }

    public void onSwipeRefresh() {
        if(loading.getValue() == null || !loading.getValue()) {
            leaderboardLiveData.setValue(null);
            getLeaderboard();
        }
    }

    public void setLeaderboard(ArrayList<Score> leaderboard) {
        loading.setValue(false);
        leaderboardLiveData.setValue(leaderboard);
        if(getLeaderboardAsynctask != null) {
            getLeaderboardAsynctask.cancel(true);
        }
        getLeaderboardAsynctask = null;
    }

}
