package djordjeh.architecture.mvp.ui.tasks;

import android.content.Intent;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowToast;

import java.util.Arrays;
import java.util.List;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import djordjeh.architecture.mvp.ActivityTest;
import djordjeh.architecture.mvp.R;
import djordjeh.architecture.mvp.data.model.Task;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static djordjeh.architecture.mvp.util.CustomMatchers.shouldRefreshing;
import static djordjeh.architecture.mvp.util.CustomMatchers.withItemCount;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class TasksFragmentTest {

    private static final Throwable ERROR = new Throwable("No Internet connection");
    private static final Task TASK = new Task(1, "Title", "Description", false);
    private static final Task TASK_2 = new Task(2, "Title2", "Description2", true);
    private static final List<Task> TASK_LIST = Arrays.asList(TASK, TASK_2);

    private TasksFragment fragment = TasksFragment.newInstance();

    @Mock
    TasksContract.Presenter presenter;

    @Rule
    public ActivityTestRule<ActivityTest> activityRule = new ActivityTestRule<ActivityTest>(ActivityTest.class, true, false){

        @Override
        protected void afterActivityLaunched() {
            getActivity().startFragment(fragment, instance -> {
                if (instance instanceof TasksFragment) {
                    fragment.presenter = presenter;
                }
            });
        }
    };

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), ActivityTest.class);
        activityRule.launchActivity(intent);
    }


    @Test
    public void emptyListViewIsShowed() {
        onView(withId(R.id.recyclerTasks)).check(matches(withItemCount(0)));
        onView(withId(R.id.textEmptyList)).check(matches(isDisplayed()));
    }

    @Test
    public void showTasksAndHideEmptyView() {
        verify(presenter).getTasks(false);
        fragment.showTasks(TASK_LIST);
        onView(withId(R.id.recyclerTasks)).check(matches(withItemCount(2)));
        onView(withId(R.id.textEmptyList)).check(matches(not(isDisplayed())));
        onView(withText(TASK.getTitle())).check(matches(isDisplayed()));
        onView(withText(TASK_2.getTitle())).check(matches(isDisplayed()));
    }

    @Test
    public void pullToRefreshShouldRefreshTheList() {
        onView(withId(R.id.recyclerTasks)).perform(swipeDown());
        onView(withId(R.id.refreshLayout)).check(matches(shouldRefreshing(true)));
//        verify(presenter).onRefresh();
    }

    @Test
    public void shouldTurnOffRefreshing() {
        fragment.showProgress(false);
        onView(withId(R.id.refreshLayout)).check(matches(shouldRefreshing(false)));
    }

    @Test
    public void buttonAddTask() {
        onView(withId(R.id.buttonAdd)).perform(click());
        verify(presenter).addNewTask();
    }

    @Test
    public void showSavedMessageWithCompletedTask() {
        fragment.showSavedMessage(TASK_2);
        assertTrue(ShadowToast.showedToast(fragment.getString(R.string.task_completed, TASK_2.getTitle())));
    }

    @Test
    public void showSavedMessageWithNotCompletedTask() {
        fragment.showSavedMessage(TASK);
        assertTrue(ShadowToast.showedToast(fragment.getString(R.string.task_not_completed, TASK.getTitle())));
    }

    @Test
    public void showError() {
        fragment.showError(ERROR);
        assertTrue(ShadowToast.showedToast(ERROR.getLocalizedMessage()));
    }

    @Test
    public void showDeleteMessage() {
        fragment.showTaskDeletedMessage(TASK);
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.task_deleted)));
    }

    @Test
    public void undoActionDelete() {
        fragment.showTaskDeletedMessage(TASK);
        onView(withId(com.google.android.material.R.id.snackbar_action)).perform(click());
        verify(presenter).saveTask(TASK);
    }

    @After
    public void finish() {
        activityRule.finishActivity();
    }
}
