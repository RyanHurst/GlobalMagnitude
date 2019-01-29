package ryanhurst.globalmagnitude.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import ryanhurst.globalmagnitude.GmHelper;
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
        return GmHelper.formatScore(scoreModel.score);
    }

    @Bindable
    public String getMatchTime() {
        return GmHelper.formatElapsedTime(scoreModel.matchTime);
    }
}
