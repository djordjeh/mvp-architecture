package djordjeh.architecture.mvp.ui.task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.ui.SchedulersFacade;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.verify;

public class TaskDetailsPresenterTest {

    private static final Task TASK = new Task(1, "Title", "Description", false);

    @Mock
    private ContractTask.View view;

    @Mock
    private TaskDataSource taskDataSource;

    @Mock
    private SchedulersFacade schedulersFacade;

    @InjectMocks
    private TaskPresenter presenter;

    @Before
    public void setupPresenterTask() {
        MockitoAnnotations.initMocks(this);

        // SchedulersFacade
        Mockito.doReturn(Schedulers.trampoline()).when(schedulersFacade).io();
        Mockito.doReturn(Schedulers.trampoline()).when(schedulersFacade).mainThread();
    }

    @Test
    public void getTaskAndShowIntoView() {
        Mockito.doReturn(Maybe.just(TASK)).when(taskDataSource).task(1);
        presenter.getTask(1);
        verify(taskDataSource).task(1);
        verify(view).showTask(TASK);
    }

    @Test
    public void getTaskWithError() {
        final Throwable throwable = new Throwable("Task not found");
        Mockito.doReturn(Maybe.error(throwable)).when(taskDataSource).task(1);
        presenter.getTask(1);
        verify(view).showError(throwable);
    }

    @Test
    public void saveTaskWithEmptyTitle() {
        final Task task = new Task();
        presenter.saveTask(task);
        verify(taskDataSource, Mockito.never()).save(task);
        verify(view).showEmptyTitleError(true);
    }

    @Test
    public void saveTaskAndShowIntoView() {
        Mockito.doReturn(Single.just(TASK)).when(taskDataSource).save(TASK);
        presenter.saveTask(TASK);
        verify(taskDataSource).save(TASK);
        verify(view).showEmptyTitleError(false);
        verify(view).onTaskSaved(TASK);
    }

    @Test
    public void saveTaskWithErrorAndShotIntoView() {
        final Throwable throwable = new Throwable("No Internet connection");
        Mockito.doReturn(Single.error(throwable)).when(taskDataSource).save(TASK);
        presenter.saveTask(TASK);
        verify(view).showError(throwable);
    }

    @After
    public void destroy() {
        presenter.destroy();
    }
}
