package ryanhurst.globalmagnitude;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ryanhurst.globalmagnitude.databinding.FragmentLeaderboardBinding;
import ryanhurst.globalmagnitude.databinding.RowLeaderboardBinding;

/**
 * A placeholder fragment containing a simple view.
 */
public class LeaderboardFragment extends Fragment implements Observer<ArrayList<Score>> {

    public static final String TAG = "LeaderboardFragment";

    private LeaderboardViewModel leaderboardViewModel;
    private FragmentLeaderboardBinding binding;

    public LeaderboardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leaderboardViewModel = ViewModelProviders.of(getActivity()).get(LeaderboardViewModel.class);
        leaderboardViewModel.getLeaderboard().observe(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leaderboard, container, false);
        binding.setVm(leaderboardViewModel);

        return binding.getRoot();
    }

    @Override
    public void onChanged(@Nullable ArrayList<Score> leaderboard) {
        RecyclerView.Adapter adapter = binding.leaderboardRecyclerView.getAdapter();
        if(adapter == null) {
            binding.leaderboardRecyclerView.setHasFixedSize(true);
            binding.leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.leaderboardRecyclerView.setAdapter(new LeaderboardAdapter(leaderboard));
        } else {
            ((LeaderboardAdapter)adapter).setLeaderboard(leaderboard);
        }
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
