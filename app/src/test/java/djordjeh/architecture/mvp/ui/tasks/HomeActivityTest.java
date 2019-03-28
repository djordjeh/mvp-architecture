package djordjeh.architecture.mvp.ui.tasks;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import djordjeh.architecture.mvp.R;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
public class HomeActivityTest {

    private HomeActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(HomeActivity.class).create().resume().get();
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveTasksFragment() {
        assertNotNull(activity.getSupportFragmentManager().findFragmentById(R.id.container));
    }
}
