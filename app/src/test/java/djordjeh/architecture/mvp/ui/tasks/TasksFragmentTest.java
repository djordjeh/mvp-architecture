package djordjeh.architecture.mvp.ui.tasks;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import djordjeh.architecture.mvp.R;
import djordjeh.architecture.mvp.TestApplication;
import djordjeh.architecture.mvp.ui.task.TaskFragment;

@Config(application = TestApplication.class)
@RunWith(RobolectricTestRunner.class)
public class TasksFragmentTest {

    private final SupportFragmentController<TasksFragment> controller = SupportFragmentController.of(new TasksFragment(), HomeActivity.class).create(R.id.container, new Bundle());

    @Mock
    private TasksContract.Presenter presenter;

    @InjectMocks
    private TasksFragment fragment = controller.get();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        controller.start().resume();
    }

    @Test
    public void testStart() {
        Mockito.verify(presenter).getTasks(false);
    }

    @Test
    public void clickOnNewTaskButton() {
        fragment.showTask(null);
        assertNotNull("TaskFragment doesn't exists", fragment.getChildFragmentManager().findFragmentByTag(TaskFragment.TAG));
    }

    @Test
    public void testShowToastError() {
        fragment.showError(new Throwable("test message"));
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo("test message"));
    }

    @Test
    public void showProgressIntoView() {
        fragment.showProgress(true);
        assertTrue(((SwipeRefreshLayout)fragment.getView().findViewById(R.id.refreshLayout)).isRefreshing());
    }
}
