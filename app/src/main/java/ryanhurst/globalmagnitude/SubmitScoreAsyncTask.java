package ryanhurst.globalmagnitude;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import ryanhurst.globalmagnitude.models.Score;
import ryanhurst.globalmagnitude.viewmodels.TriviaViewModel;

/**
 * AsyncTask to submit score
 *
 * Created by Ryan on 11/9/2017.
 */

public class SubmitScoreAsyncTask extends AsyncTask<Score, Boolean, Boolean> {

    private WeakReference<TriviaViewModel> triviaViewModelWeakReference;

    public SubmitScoreAsyncTask(TriviaViewModel triviaViewModel) {
        this.triviaViewModelWeakReference = new WeakReference<TriviaViewModel>(triviaViewModel);
    }

    @Override
    protected Boolean doInBackground(Score... scores) {
        return GmRestHelper.submitScore(scores[0]);
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        TriviaViewModel triviaViewModel = triviaViewModelWeakReference.get();

        if(triviaViewModel != null) {
            triviaViewModel.scoreSubmitted(success);
        }
    }
}
