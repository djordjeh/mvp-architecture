package djordjeh.architecture.mvp.ui.task;

import android.content.Context;
import android.content.Intent;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowToast;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import djordjeh.architecture.mvp.ActivityTest;
import djordjeh.architecture.mvp.R;
import djordjeh.architecture.mvp.data.model.Task;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static djordjeh.architecture.mvp.util.CustomMatchers.isErrorDisplayed;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class TaskFragmentTest {

    private static final Throwable ERROR = new Throwable("No Internet connection");
    private static final Task TASK = new Task(1, "Task title", "Task description", false);

    private TaskFragment fragment = TaskFragment.newInstance(TASK);

    private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Mock
    ContractTask.Presenter presenter;

    @Rule
    public ActivityTestRule<ActivityTest> activityRule = new ActivityTestRule<ActivityTest>(ActivityTest.class, true, false){

        @Override
        protected void afterActivityLaunched() {
            getActivity().startFragment(fragment, instance -> {
                if (instance instanceof TaskFragment) {
                    fragment.presenter = presenter;
                }
            });
        }
    };

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Intent intent = new Intent(context, ActivityTest.class);
        activityRule.launchActivity(intent);
    }

    @Test
    public void checkViews_AndGetTaskFromPresenter() {
        onView(withId(R.id.editTextTitle)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextDescription)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonSubmit)).check(matches(isDisplayed()));
        verify(presenter).getTask(TASK.getId());
    }

    @Test
    public void showTaskTest_andAfterScreenRotation() {
        fragment.showTask(TASK);
        onView(withId(R.id.editTextTitle)).check(matches(withText(TASK.getTitle())));
        onView(withId(R.id.editTextDescription)).check(matches(withText(TASK.getDescription())));
    }

    @Test
    public void showEmptyTitleErrorTest_andTypeNewTitle() {
        fragment.showEmptyTitleError(true);
        onView(withId(R.id.inputTitle)).check(matches(isErrorDisplayed(context.getString(R.string.error_empty_title))));

        onView(withId(R.id.editTextTitle)).perform(clearText(), typeText(TASK.getTitle()), closeSoftKeyboard());
        onView(withId(R.id.inputTitle)).check(matches(isErrorDisplayed(null)));
    }

    @Test
    public void onTaskSavedShowMessageTest() {
        fragment.onTaskSaved(TASK);
        assertTrue(ShadowToast.showedToast(context.getString(R.string.task_saved, TASK.getTitle())));
    }

    @Test
    public void showError() {
        fragment.showError(ERROR);
        assertTrue(ShadowToast.showedToast(ERROR.getMessage()));
    }
}
