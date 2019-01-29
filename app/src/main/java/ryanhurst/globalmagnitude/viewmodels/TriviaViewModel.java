package ryanhurst.globalmagnitude.viewmodels;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import androidx.databinding.Bindable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ryanhurst.globalmagnitude.BR;
import ryanhurst.globalmagnitude.GmHelper;
import ryanhurst.globalmagnitude.models.Score;
import ryanhurst.globalmagnitude.models.TriviaGame;

/**
 * trivia view model
 *
 * Created by Ryan on 11/7/2017.
 */

public class TriviaViewModel extends ObservableViewModel {

    private static final String TAG = "TriviaViewModel";

    private TriviaGame triviaGameModel = new TriviaGame();

    private MutableLiveData<ArrayList<Integer>> currentAnswersLiveData;
    private MutableLiveData<Boolean> submittedScoreLiveData;
    private AsyncTask<Void, Void, Void> submitScoreAsyncTask;

    @Bindable
    public String username;

    @Bindable
    public boolean loading;

    public TriviaViewModel() {
    }

    @Bindable
    public String getNumberCorrect() {
        int numberCorrect = GmHelper.getNumberCorrect(triviaGameModel);
        return numberCorrect + "/" + TriviaGame.NUMBER_OF_ROUNDS;
    }

    @Bindable
    public String getScore() {
        double score = calculateScore();
        return GmHelper.formatScore(score);
    }

    @Bindable
    public String getMatchTime() {
        long elapsedMillis = triviaGameModel.endTime - triviaGameModel.startTime;
        return GmHelper.getElapsedSeconds(elapsedMillis);
    }

    @Bindable
    public String getAverageQuestionTime() {
        long elapsedMillis = triviaGameModel.endTime - triviaGameModel.startTime;
        long averageMillis = elapsedMillis/TriviaGame.NUMBER_OF_ROUNDS;
        return GmHelper.getElapsedSeconds(averageMillis);
    }

    @Bindable
    public String getFactor1() {
        return triviaGameModel.getCurrentRound().factor1 + "";
    }

    @Bindable
    public String getFactor2() {
        return triviaGameModel.getCurrentRound().factor2 + "";
    }

    @Bindable
    public String getTime() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - triviaGameModel.startTime;

        return GmHelper.formatElapsedTime(elapsed);
    }

    @Bindable
    public String getCurrentRoundIndex() {
        return (triviaGameModel.currentRoundIndex + 1) + "";
    }

    @Bindable
    public String getNumberOfRounds() {
        return TriviaGame.NUMBER_OF_ROUNDS + "";
    }

    public void answerQuestion(int index) {
        triviaGameModel.getCurrentRound().userAnswerIndex = index;
        if(triviaGameModel.currentRoundIndex < TriviaGame.NUMBER_OF_ROUNDS - 1) {
            triviaGameModel.currentRoundIndex++;
            notifyChange();
            currentAnswersLiveData.setValue(triviaGameModel.getCurrentRound().answers);
        } else {
            triviaGameModel.endTime = System.currentTimeMillis();
        }
    }

    public boolean usernameValid() {
        return !username.isEmpty();
    }

    public boolean gameOver() {
        return triviaGameModel.currentRoundIndex == TriviaGame.NUMBER_OF_ROUNDS - 1 &&
                triviaGameModel.getCurrentRound().userAnswerIndex != -1;
    }

    public LiveData<ArrayList<Integer>> getAnswers() {
        if(currentAnswersLiveData == null) {
            currentAnswersLiveData = new MutableLiveData<>();
            currentAnswersLiveData.setValue(triviaGameModel.getCurrentRound().answers);
        }

        return currentAnswersLiveData;
    }

    private double calculateScore() {
        int numberCorrect = GmHelper.getNumberCorrect(triviaGameModel);
        return ((double) numberCorrect) / TriviaGame.NUMBER_OF_ROUNDS;
    }

    @SuppressLint("StaticFieldLeak")
    public LiveData<Boolean> submitScore(final Context context) {
        GmHelper.setUsername(context, username);
        final Score score = new Score(username, calculateScore(), triviaGameModel.endTime - triviaGameModel.startTime);
        if(submitScoreAsyncTask == null) {
            loading = true;
            notifyPropertyChanged(BR.loading);

            submittedScoreLiveData = new MutableLiveData<>();
            submitScoreAsyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    GmHelper.getDatabase(context).scoreDao().insertAll(score);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    submittedScoreLiveData.setValue(true);
                    submitScoreAsyncTask = null;
                    loading = false;
                    notifyPropertyChanged(BR.loading);
                }
            };
            submitScoreAsyncTask.execute();
        }

        return submittedScoreLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(submitScoreAsyncTask != null) {
            submitScoreAsyncTask.cancel(true);
            submitScoreAsyncTask = null;
        }
    }
}
