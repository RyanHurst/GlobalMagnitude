package ryanhurst.globalmagnitude;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ryanhurst.globalmagnitude.models.Score;
import ryanhurst.globalmagnitude.viewmodels.LeaderboardViewModel;

/**
 * AsyncTask to get the leaderboard
 *
 * Created by Ryan on 11/6/2017.
 */

public class GetLeaderboardAsyncTask extends AsyncTask<Void, ArrayList<Score>, ArrayList<Score>> {

    private WeakReference<LeaderboardViewModel> leaderboardViewModelWeakReference;

    public GetLeaderboardAsyncTask(LeaderboardViewModel leaderboardViewModel) {
        this.leaderboardViewModelWeakReference = new WeakReference<LeaderboardViewModel>(leaderboardViewModel);
    }

    @Override
    protected ArrayList<Score> doInBackground(Void... voids) {
        ArrayList<Score> leaderboard = GmRestHelper.getLeaderboard();
        return leaderboard;
    }

    @Override
    protected void onPostExecute(ArrayList<Score> leaderboard) {
        super.onPostExecute(leaderboard);

        LeaderboardViewModel leaderboardViewModel = leaderboardViewModelWeakReference.get();

        if(leaderboardViewModel != null) {
            leaderboardViewModel.setLeaderboard(leaderboard);
        }
    }
}
