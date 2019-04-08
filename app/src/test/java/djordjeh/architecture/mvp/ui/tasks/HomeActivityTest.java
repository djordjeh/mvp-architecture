package djordjeh.architecture.mvp.ui.tasks;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class HomeActivityTest {

    @Rule
    public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    public void shouldHaveTasksFragment() {
        assertNotNull("TasksFragment not added", activityRule.getActivity().getSupportFragmentManager().findFragmentByTag(TasksFragment.TAG));
    }


}
