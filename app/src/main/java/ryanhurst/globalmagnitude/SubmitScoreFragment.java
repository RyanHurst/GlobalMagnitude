package ryanhurst.globalmagnitude;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ryanhurst.globalmagnitude.databinding.FragmentSubmitScoreBinding;
import ryanhurst.globalmagnitude.models.Score;
import ryanhurst.globalmagnitude.viewmodels.TriviaViewModel;


/**
 * Submit score fragment. Displays stats and submits score
 */
public class SubmitScoreFragment extends Fragment implements Observer<Boolean> {

    public static final String TAG = "SubmitScoreFragment";

    private TriviaViewModel triviaViewModel;

    public SubmitScoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        triviaViewModel = ViewModelProviders.of(getActivity()).get(TriviaViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final FragmentSubmitScoreBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_submit_score, container, false);
        binding.setVm(triviaViewModel);

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(triviaViewModel.usernameValid()) {
                    Score score = new Score(triviaViewModel.username, triviaViewModel.getScore(),
                            triviaViewModel.getMatchTime(), triviaViewModel.getAverageQuestionTime());
                    triviaViewModel.submitScore(score)
                            .observe(SubmitScoreFragment.this, SubmitScoreFragment.this);
                } else {
                    binding.usernameEditText.setError(getString(R.string.username_is_required));
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onChanged(@Nullable Boolean success) {
        if(success != null && success) {
            CharSequence text = getString(R.string.score_submitted);
            Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            toast.show();

            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            CharSequence text = getString(R.string.error);
            Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
