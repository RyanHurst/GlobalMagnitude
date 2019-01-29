package ryanhurst.globalmagnitude;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Trivia Activity
 */
public class TriviaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new TriviaFragment(), TriviaFragment.TAG)
                    .commit();
        }
    }
}
