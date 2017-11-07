package ryanhurst.globalmagnitude;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Ryan on 11/6/2017.
 */

public class GetLeaderboardAsyncTask extends AsyncTask<Void, ArrayList<Leaderboard>, ArrayList<Leaderboard>> {

    private WeakReference<LeaderboardViewModel> leaderboardViewModelWeakReference;

    GetLeaderboardAsyncTask(LeaderboardViewModel leaderboardViewModel) {
        this.leaderboardViewModelWeakReference = new WeakReference<LeaderboardViewModel>(leaderboardViewModel);
    }

    @Override
    protected ArrayList<Leaderboard> doInBackground(Void... voids) {
        ArrayList<Leaderboard> leaderboard = GmRestHelper.getLeaderboard();
        return leaderboard;
    }

    @Override
    protected void onPostExecute(ArrayList<Leaderboard> leaderboards) {
        super.onPostExecute(leaderboards);

        LeaderboardViewModel leaderboardViewModel = leaderboardViewModelWeakReference.get();

        if(leaderboardViewModel != null) {
            leaderboardViewModel.setLeaderboard(leaderboards);
        }
    }
}
