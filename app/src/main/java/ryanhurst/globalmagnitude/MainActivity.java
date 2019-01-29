package ryanhurst.globalmagnitude;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Main Activity
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, leaderboardFragment, LeaderboardFragment.TAG)
                    .commit();
        }
    }
}
