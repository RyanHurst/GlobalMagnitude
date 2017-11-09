package ryanhurst.globalmagnitude.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.SharedPreferences;
import android.databinding.Bindable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ryanhurst.globalmagnitude.BR;
import ryanhurst.globalmagnitude.GmHelper;
import ryanhurst.globalmagnitude.SubmitScoreAsyncTask;
import ryanhurst.globalmagnitude.models.Score;
import ryanhurst.globalmagnitude.models.TriviaGame;

/**
 * trivia view model
 *
 * Created by Ryan on 11/7/2017.
 */

public class TriviaViewModel extends BaseObservableViewModel {

    private static final String TAG = "TriviaViewModel";
    private static final String USERNAME_KEY = "username";

    private TriviaGame triviaGameModel = new TriviaGame();

    private MutableLiveData<ArrayList<Integer>> currentAnswersLiveData;
    private MutableLiveData<Boolean> submittedScoreLiveData;
    private SubmitScoreAsyncTask submitScoreAsyncTask;

    @Bindable
    public String username;

    @Bindable
    public boolean loading;

    public TriviaViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application);
        username = preferences.getString(USERNAME_KEY, "");
    }

    @Bindable
    public String getScore() {
        int numberCorrect = 0;

        for(TriviaGame.Round r : triviaGameModel.rounds) {
            int userAnswer = r.answers.get(r.userAnswerIndex);
            if(r.factor1 * r.factor2 == userAnswer) {
                numberCorrect++;
            }
        }

        int percent = (numberCorrect * 100) / TriviaGame.NUMBER_OF_ROUNDS;
        return percent + "%";
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

    public LiveData<Boolean> submitScore(Score score) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplication());
        preferences.edit()
                .putString(USERNAME_KEY, username)
                .apply();

        if(submitScoreAsyncTask == null) {
            loading = true;
            notifyPropertyChanged(BR.loading);

            submittedScoreLiveData = new MutableLiveData<>();
            submitScoreAsyncTask = new SubmitScoreAsyncTask(this);

            submitScoreAsyncTask.execute(score);
        }

        return submittedScoreLiveData;
    }

    public void scoreSubmitted(Boolean success) {
        submittedScoreLiveData.setValue(success);
        submitScoreAsyncTask = null;
        loading = false;
        notifyPropertyChanged(BR.loading);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        submitScoreAsyncTask.cancel(true);
        submitScoreAsyncTask = null;
    }
}
