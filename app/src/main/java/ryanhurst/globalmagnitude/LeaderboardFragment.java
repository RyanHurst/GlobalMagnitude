package ryanhurst.globalmagnitude;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ryanhurst.globalmagnitude.databinding.FragmentLeaderboardBinding;
import ryanhurst.globalmagnitude.databinding.RowLeaderboardBinding;
import ryanhurst.globalmagnitude.models.Score;
import ryanhurst.globalmagnitude.viewmodels.ScoreViewModel;

/**
 * Leaderboard Fragment
 */
public class LeaderboardFragment extends Fragment implements Observer<List<Score>> {

    public static final String TAG = "LeaderboardFragment";

    private FragmentLeaderboardBinding binding;

    public LeaderboardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_leaderboard, container, false);

        binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), TriviaActivity.class));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: 1/29/19 for some reason, the livedata only seems to get updated once.
        // it doesn't update when the database updates. Move this to onCreate if that changes
        GmHelper.getDatabase(getActivity()).scoreDao().getAll().observe(this, this);
    }

    @Override
    public void onChanged(@Nullable List<Score> leaderboard) {
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

    public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardViewHolder>{

        private List<Score> leaderboard;

        LeaderboardAdapter(@Nullable List<Score> leaderboard) {
            setLeaderboard(leaderboard);
        }

        private void setLeaderboard(@Nullable List<Score> leaderboard) {
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
