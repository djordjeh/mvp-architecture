package djordjeh.architecture.mvp.ui.tasks;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.ui.SchedulersFacade;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class TasksPresenterTest {

    private static final Throwable throwable = new Throwable("No Internet connection");
    private static final Task task = new Task(1, "Title", "Description", false);
    private static final List<Task> tasksResult = new ArrayList<Task>() {{
        add(task);
    }};

    @Mock
    private TasksContract.View view;

    @Mock
    private TaskDataSource taskDataSource;

    @Mock
    private SchedulersFacade schedulersFacade;

    @InjectMocks
    private TasksPresenter presenter;

    @Before
    public void setupPresenterTasks() {
        MockitoAnnotations.initMocks(this);

        // SchedulersFacade
        Mockito.doReturn(Schedulers.trampoline()).when(schedulersFacade).io();
        Mockito.doReturn(Schedulers.trampoline()).when(schedulersFacade).mainThread();
    }

    @Test
    public void loadTasksFromRemoteDataSourceAndLoadIntoView() {
        final boolean forceUpdate = true;
        Mockito.doReturn(Observable.just(tasksResult)).when(taskDataSource).tasks(forceUpdate);
        presenter.getTasks(forceUpdate);
        verify(taskDataSource).tasks(forceUpdate);

        InOrder inOrder = Mockito.inOrder(view);
        inOrder.verify(view).showProgress(true);
        inOrder.verify(view).showTasks(tasksResult);
        inOrder.verify(view).showProgress(false);
    }

    @Test
    public void observeTasksLocallyAndLoadIntoView() {
        final boolean forceUpdate = false;
        Mockito.doReturn(Observable.just(tasksResult)).when(taskDataSource).tasks(forceUpdate);
        presenter.getTasks(forceUpdate);
        verify(taskDataSource).tasks(forceUpdate);

        verify(view, never()).showProgress(true);
        verify(view).showTasks(tasksResult);
    }

    @Test
    public void showLoadTasksError() {
        Mockito.doReturn(Observable.error(throwable)).when(taskDataSource).tasks(true);
        presenter.getTasks(true);
        verify(view).showError(throwable);
    }

    @Test
    public void saveTaskInRepoAndShowIntoView() {
        Mockito.doReturn(Single.just(task)).when(taskDataSource).save(task);
        presenter.saveTask(task);
        verify(taskDataSource).save(task);
        verify(view).showSavedMessage(task);
    }

    @Test
    public void saveTaskWithError() {
        Mockito.doReturn(Single.error(throwable)).when(taskDataSource).save(task);
        presenter.saveTask(task);
        verify(view).showError(throwable);
    }

    @Test
    public void deleteTaskAndShowMessageIntoView() {
        Mockito.doReturn(Single.just(task)).when(taskDataSource).delete(task);
        presenter.deleteTask(task);
        verify(taskDataSource).delete(task);
        verify(view).showTaskDeletedMessage(task);
    }

    @Test
    public void deleteTaskWithError() {
        Mockito.doReturn(Single.error(throwable)).when(taskDataSource).delete(task);
        presenter.deleteTask(task);
        verify(view).showError(throwable);
    }

    @Test
    public void addTaskShowIntoView() {
        presenter.addNewTask();
        verify(view).showTask(null);
    }

    @Test
    public void showTaskIntoView() {
        presenter.showTask(task);
        verify(view).showTask(task);
    }

    @Test
    public void refreshTasksAndShowIntoView() {
        Mockito.doReturn(Observable.just(tasksResult)).when(taskDataSource).tasks(true);
        presenter.onRefresh();
        verify(taskDataSource).tasks(true);
    }

    @After
    public void destroy() {
        presenter.destroy();
    }
}
