package ryanhurst.globalmagnitude;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ryanhurst.globalmagnitude.databinding.FragmentLeaderboardBinding;
import ryanhurst.globalmagnitude.databinding.RowLeaderboardBinding;
import ryanhurst.globalmagnitude.models.Score;
import ryanhurst.globalmagnitude.viewmodels.LeaderboardViewModel;
import ryanhurst.globalmagnitude.viewmodels.ScoreViewModel;

import static android.app.Activity.RESULT_OK;

/**
 * Leaderboard Fragment
 */
public class LeaderboardFragment extends Fragment {

    public static final String TAG = "LeaderboardFragment";
    private static final int TRIVIA_REQUEST = 101;

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;

    public LeaderboardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leaderboardViewModel = ViewModelProviders.of(getActivity()).get(LeaderboardViewModel.class);
        leaderboardViewModel.getLeaderboard().observe(this, new Observer<ArrayList<Score>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Score> leaderboard) {
                if(leaderboard != null) {
                    ViewCompat.setNestedScrollingEnabled(binding.leaderboardRecyclerView, false);

                    RecyclerView.Adapter adapter = binding.leaderboardRecyclerView.getAdapter();
                    if(adapter == null) {
                        binding.leaderboardRecyclerView.setHasFixedSize(true);
                        binding.leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        binding.leaderboardRecyclerView.setAdapter(new LeaderboardAdapter(leaderboard));
                    } else {
                        ((LeaderboardAdapter) adapter).setLeaderboard(leaderboard);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leaderboard, container, false);
        binding.setVm(leaderboardViewModel);

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TriviaActivity.class);
                startActivityForResult(intent, TRIVIA_REQUEST);
            }
        });

        leaderboardViewModel.loading.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean loading) {
                if(loading == null) {
                    loading = false;
                }
                binding.swipeRefresh.setRefreshing(loading);
            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leaderboardViewModel.onSwipeRefresh();
            }
        });

        return binding.getRoot();
    }

    public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardViewHolder>{

        private ArrayList<Score> leaderboard;

        LeaderboardAdapter(@Nullable ArrayList<Score> leaderboard) {
            setLeaderboard(leaderboard);
        }

        private void setLeaderboard(@Nullable ArrayList<Score> leaderboard) {
            if(leaderboard == null) {
                this.leaderboard = new ArrayList<>();
            } else {
                this.leaderboard = leaderboard;
            }
            notifyDataSetChanged();
        }

        @Override
        public LeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new LeaderboardViewHolder(RowLeaderboardBinding.inflate(inflater, parent, false));
        }

        @Override
        public void onBindViewHolder(LeaderboardViewHolder holder, int position) {
            holder.bind(leaderboard.get(position));
        }

        @Override
        public int getItemCount() {
            if(leaderboard == null) {
                return 0;
            }
            return leaderboard.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == TRIVIA_REQUEST) {
            leaderboardViewModel.onSwipeRefresh();
        }
    }

    public class LeaderboardViewHolder extends RecyclerView.ViewHolder{

        private RowLeaderboardBinding binding;

        public LeaderboardViewHolder(RowLeaderboardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Score score) {
            binding.executePendingBindings();
            binding.setVm(new ScoreViewModel(score));
        }
    }
}
