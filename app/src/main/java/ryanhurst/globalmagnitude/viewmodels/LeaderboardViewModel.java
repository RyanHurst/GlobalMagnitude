package ryanhurst.globalmagnitude.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ryanhurst.globalmagnitude.GetLeaderboardAsyncTask;
import ryanhurst.globalmagnitude.models.Score;

/**
 * ViewModel for leaderboard
 * <p>
 * Created by Ryan on 11/6/2017.
 */

public class LeaderboardViewModel extends BaseObservableViewModel {

    private GetLeaderboardAsyncTask getLeaderboardAsynctask;

    public MutableLiveData<Boolean> loading = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Score>> leaderboardLiveData = new MutableLiveData<>();

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
    }

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
