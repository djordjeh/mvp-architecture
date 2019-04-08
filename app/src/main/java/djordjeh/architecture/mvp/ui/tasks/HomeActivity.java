package djordjeh.architecture.mvp.ui.tasks;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import dagger.android.support.DaggerAppCompatActivity;
import djordjeh.architecture.mvp.R;

public class HomeActivity extends DaggerAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment == null) {
            fragmentManager.beginTransaction().add(R.id.container, TasksFragment.newInstance(), TasksFragment.TAG).commit();
        }
    }
}
