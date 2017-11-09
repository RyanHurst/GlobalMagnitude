package ryanhurst.globalmagnitude;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
