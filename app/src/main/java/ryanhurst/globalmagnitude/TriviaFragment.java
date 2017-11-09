package ryanhurst.globalmagnitude;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ryanhurst.globalmagnitude.databinding.FragmentTriviaBinding;
import ryanhurst.globalmagnitude.databinding.RowAnswerBinding;
import ryanhurst.globalmagnitude.viewmodels.TriviaViewModel;


/**
 * Trivia Fragment. Contains trivia questions and answers
 */
public class TriviaFragment extends Fragment implements Observer<ArrayList<Integer>> {

    public static final String TAG = "TriviaFragment";

    private FragmentTriviaBinding binding;
    private TriviaViewModel triviaViewModel;

    private Handler handler;

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            triviaViewModel.notifyPropertyChanged(BR.time);
            handler.postDelayed(updateTimer, 100);
        }
    };

    public TriviaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        triviaViewModel = ViewModelProviders.of(getActivity()).get(TriviaViewModel.class);
        triviaViewModel.getAnswers().observe(this, this);
        handler = new Handler();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTimer.run();
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(updateTimer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_trivia, container, false);
        binding.setVm(triviaViewModel);

        return binding.getRoot();
    }

    @Override
    public void onChanged(@Nullable ArrayList<Integer> answers) {
        RecyclerView.Adapter adapter = binding.triviaAnswers.getAdapter();
        if(adapter == null) {
            binding.triviaAnswers.setHasFixedSize(true);
            binding.triviaAnswers.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            binding.triviaAnswers.setAdapter(new TriviaAdapter(answers));
        } else {
            ((TriviaAdapter)adapter).setAnswers(answers);
        }
    }


    public class TriviaAdapter extends RecyclerView.Adapter<TriviaViewHolder>{

        private ArrayList<Integer> answers;

        TriviaAdapter(@Nullable ArrayList<Integer> answers) {
            setAnswers(answers);
        }

        private void setAnswers(@Nullable ArrayList<Integer> trivia) {
            if(trivia == null) {
                this.answers = new ArrayList<>();
            } else {
                this.answers = trivia;
            }
            notifyDataSetChanged();
        }

        @Override
        public TriviaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new TriviaViewHolder(RowAnswerBinding.inflate(inflater, parent, false));
        }

        @Override
        public void onBindViewHolder(TriviaViewHolder holder, int position) {
            holder.bind(answers.get(position) + "", position);
        }

        @Override
        public int getItemCount() {
            if(answers == null) {
                return 0;
            }
            return answers.size();
        }
    }

    public class TriviaViewHolder extends RecyclerView.ViewHolder{

        private RowAnswerBinding binding;

        public TriviaViewHolder(RowAnswerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String answer, final int position) {
            binding.executePendingBindings();
            binding.answerButton.setText(answer);
            binding.answerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    triviaViewModel.answerQuestion(position);
                    if(triviaViewModel.gameOver()) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.trivia_container, new SubmitScoreFragment(), SubmitScoreFragment.TAG)
                                .commit();
                    }
                }
            });
        }
    }
}
