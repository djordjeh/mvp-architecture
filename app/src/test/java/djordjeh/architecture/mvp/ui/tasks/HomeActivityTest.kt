package djordjeh.architecture.mvp.ui.tasks

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule

import org.junit.Assert.assertNotNull

@RunWith(AndroidJUnit4::class)
@SmallTest
class HomeActivityTest {

    @get:Rule
    var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun shouldHaveTasksFragment() {
        assertNotNull("TasksFragment not added", activityRule.activity.supportFragmentManager.findFragmentByTag(TasksFragment.TAG))
    }


}
