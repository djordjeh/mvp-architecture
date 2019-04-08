package djordjeh.architecture.mvp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class ActivityTest extends AppCompatActivity implements HasSupportFragmentInjector {

    private AndroidInjector<Fragment> supportFragmentInjector;

    public void startFragment(Fragment fragment, AndroidInjector<Fragment> supportFragmentInjector) {
        this.supportFragmentInjector = supportFragmentInjector;
        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, fragment, "Test")
                .commit();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return supportFragmentInjector;
    }
}
