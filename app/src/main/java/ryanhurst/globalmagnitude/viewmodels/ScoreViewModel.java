package ryanhurst.globalmagnitude.viewmodels;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ryanhurst.globalmagnitude.models.Score;

/**
 * Score ViewModel
 *
 * Created by Ryan on 11/6/2017.
 */

public class ScoreViewModel extends BaseObservable {

    private Score scoreModel;

    public ScoreViewModel(Score scoreModel) {
        this.scoreModel = scoreModel;
    }

    @Bindable
    public String getUser() {
        return scoreModel.userId;
    }

    @Bindable
    public String getScore() {
        return scoreModel.score + "%";
    }

    @Bindable
    public String getMatchTime() {
        return scoreModel.matchTime;
    }

    @Bindable
    public String getAverageQuestionTime() {
        return scoreModel.averageQuestionTime + "";
    }

}
