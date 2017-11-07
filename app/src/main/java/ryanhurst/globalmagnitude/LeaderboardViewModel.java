package ryanhurst.globalmagnitude;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Ryan on 11/6/2017.
 */

public class LeaderboardViewModel extends BaseObservableViewModel {

    private GetLeaderboardAsyncTask getLeaderboardAsynctask;

    @Bindable
    public boolean loading;

    private MutableLiveData<ArrayList<Leaderboard>> leaderboardLiveData;

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<ArrayList<Leaderboard>> getLeaderboards() {
        if(leaderboardLiveData == null && getLeaderboardAsynctask == null) {
            loading = true;
            notifyPropertyChanged(BR.loading);

            leaderboardLiveData = new MutableLiveData<>();
            getLeaderboardAsynctask = new GetLeaderboardAsyncTask(this);

            getLeaderboardAsynctask.execute();
        }

        return leaderboardLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        getLeaderboardAsynctask.cancel(true);
        getLeaderboardAsynctask = null;
    }

    void setLeaderboard(ArrayList<Leaderboard> leaderboard) {
        leaderboardLiveData.setValue(leaderboard);
    }

}
