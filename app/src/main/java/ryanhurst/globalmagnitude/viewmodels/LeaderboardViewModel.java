package ryanhurst.globalmagnitude.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ryanhurst.globalmagnitude.BR;
import ryanhurst.globalmagnitude.GetLeaderboardAsyncTask;
import ryanhurst.globalmagnitude.models.Score;

/**
 * ViewModel for leaderboard
 *
 * Created by Ryan on 11/6/2017.
 */

public class LeaderboardViewModel extends BaseObservableViewModel {

    private GetLeaderboardAsyncTask getLeaderboardAsynctask;

    @Bindable
    public boolean loading;

    private MutableLiveData<ArrayList<Score>> leaderboardLiveData;

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Score>> getLeaderboard() {
        if(getLeaderboardAsynctask == null &&
                (leaderboardLiveData == null || leaderboardLiveData.getValue() == null)) {
            if(leaderboardLiveData == null) {
                leaderboardLiveData = new MutableLiveData<>();
            } else {
                leaderboardLiveData.setValue(null);
            }

            loading = true;
            notifyPropertyChanged(BR.loading);

            getLeaderboardAsynctask = new GetLeaderboardAsyncTask(this);

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

    public void setLeaderboard(ArrayList<Score> leaderboard) {
        loading = false;
        notifyPropertyChanged(BR.loading);
        leaderboardLiveData.setValue(leaderboard);
        if(getLeaderboardAsynctask != null) {
            getLeaderboardAsynctask.cancel(true);
        }
        getLeaderboardAsynctask = null;
    }

}
