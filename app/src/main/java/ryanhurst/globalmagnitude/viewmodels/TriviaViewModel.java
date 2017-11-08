package ryanhurst.globalmagnitude.viewmodels;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.Bindable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import ryanhurst.globalmagnitude.GmHelper;
import ryanhurst.globalmagnitude.models.TriviaGame;

/**
 * trivia view model
 *
 * Created by Ryan on 11/7/2017.
 */

public class TriviaViewModel extends BaseObservableViewModel {

    private TriviaGame triviaGameModel = new TriviaGame();

    private MutableLiveData<ArrayList<Integer>> currentAnswersLiveData;

    public TriviaViewModel(@NonNull Application application) {
        super(application);
    }

    public void answerQuestion(int index) {
        if(triviaGameModel.currentRoundIndex >= TriviaGame.NUMBER_OF_ROUNDS - 1) {
            // TODO: 11/8/2017 go to next screen
        } else {
            triviaGameModel.getCurrentRound().userAnswerIndex = index;
            triviaGameModel.currentRoundIndex++;
            notifyChange();
            currentAnswersLiveData.setValue(triviaGameModel.getCurrentRound().answers);
        }
    }

    @Bindable
    public String getFactor1() {
        return triviaGameModel.getCurrentRound().factor1 + "";
    }

    @Bindable
    public String getFactor2() {
        return triviaGameModel.getCurrentRound().factor2 + "";
    }

    public LiveData<ArrayList<Integer>> getAnswers() {
        if(currentAnswersLiveData == null) {
            currentAnswersLiveData = new MutableLiveData<>();
            currentAnswersLiveData.setValue(triviaGameModel.getCurrentRound().answers);
        }

        return currentAnswersLiveData;
    }

    @Bindable
    public String getTime() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - triviaGameModel.startTime;

        return GmHelper.formatElapsedTime(elapsed);
    }

}
