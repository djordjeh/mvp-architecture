package djordjeh.architecture.mvp.data;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TaskRepositoryTest {

    // https://github.com/googlesamples/android-architecture/tree/todo-mvp-rxjava/todoapp

    private static final Task task1 = new Task(1, "Title1", "Description1", false);
    private static final Task task2 = new Task(2, "Title2", "Description2", true);
    private static final List<Task> TASKS = new ArrayList<Task>() {{
        add(task1);
        add(task2);
    }};

    private TestObserver<List<Task>> testObserver;

    @Mock
    private TaskDataSource localDataSource;

    @Mock
    private TaskDataSource remoteDataSource;

    private TaskRepository repository;

    @Before
    public void setupRepository() {
        MockitoAnnotations.initMocks(this);

        repository = new TaskRepository(localDataSource, remoteDataSource);

        testObserver = new TestObserver<>();
    }

    @Test
    public void loadTasks__repositoryAfterFirstSubscription_fromRemoteSourceAndSaveThem() {
        final boolean forceUpdate = true;
        // We are fetching data from remote data source which has result
        setTasksAvailable(remoteDataSource, forceUpdate, TASKS);

        TestObserver<List<Task>> testObserver = new TestObserver<>();
        repository.tasks(forceUpdate).subscribe(testObserver);

        // Then tasks were only requested once from remote and saved by local sources
        verify(remoteDataSource).tasks(forceUpdate);
        verify(localDataSource, never()).tasks(forceUpdate);
        verify(localDataSource).save(task1);
        verify(localDataSource).save(task2);

        testObserver.assertValue(TASKS);
    }

    @Test
    public void loadTasks__repositoryAfterFirstSubscription_fromLocalSource() {
        final boolean forceUpdate = false;
        // We are fetching data from local data source which has result
        setTasksAvailable(localDataSource, forceUpdate, TASKS);

        TestObserver<List<Task>> testObserver = new TestObserver<>();
        repository.tasks(forceUpdate).subscribe(testObserver);

        verify(localDataSource).tasks(forceUpdate);
        verify(remoteDataSource, never()).tasks(forceUpdate);

        testObserver.assertValue(TASKS);
    }

    @Test
    public void loadTasks_repositoryFromRemoteDataSource() {
        final boolean forceUpdate = true;
        // We are fetching data from local data source which has result
        setTasksAvailable(remoteDataSource, forceUpdate, TASKS);

        // When tasks are requested from the tasks repository
        repository.tasks(forceUpdate).subscribe(testObserver);

        // Then tasks are loaded from the local data source
        verify(remoteDataSource).tasks(forceUpdate);
        verify(localDataSource, never()).tasks(forceUpdate);
        testObserver.assertValue(TASKS);
    }

    @Test
    public void loadTasks_repositoryFromLocalDataSource() {
        final boolean forceUpdate = false;
        // We are fetching data from local data source which has result
        setTasksAvailable(localDataSource, forceUpdate, TASKS);

        // When tasks are requested from the tasks repository
        repository.tasks(forceUpdate).subscribe(testObserver);

        // Then tasks are loaded from the local data source
        verify(localDataSource).tasks(forceUpdate);
        verify(remoteDataSource, never()).tasks(forceUpdate);
        testObserver.assertValue(TASKS);
    }

    @Test
    public void loadTask_repositoryAfterFirstSubscription_fromLocalSource() {
        setTaskAvailable(localDataSource, task1);
        setTaskAvailable(remoteDataSource, task2);

        TestObserver<Task> testObserver = new TestObserver<>();
        repository.task(1).subscribe(testObserver);

        // Then tasks were only requested once from remote and saved by local sources
        verify(localDataSource).task(1);
        verify(remoteDataSource).task(1);

        testObserver.assertValue(task1);
    }

    @Test
    public void loadTask_repositoryAfterFirstSubscription_fromRemoteSource() {
        setTaskNotAvailable(localDataSource);
        setTaskAvailable(remoteDataSource, task2);

        TestObserver<Task> testObserver = new TestObserver<>();
        repository.task(1).subscribe(testObserver);

        // Then tasks were only requested once from remote and saved by local sources
        verify(localDataSource).task(1);
        verify(remoteDataSource).task(1);

        testObserver.assertValue(task2);
    }

    @Test
    public void saveTask() {
        final Task newTask = new Task(123, "New task", "new description", false);
        when(remoteDataSource.save(newTask)).thenReturn(Single.just(newTask));
        when(localDataSource.save(newTask)).thenReturn(Single.just(newTask));

        TestObserver<Task> testObserver = new TestObserver<>();
        repository.save(newTask).subscribe(testObserver);

        verify(remoteDataSource).save(newTask);
        verify(localDataSource).save(newTask);

        testObserver.assertValue(newTask);
    }

    @Test
    public void deleteTask() {
        final Task dTask = new Task(321, "Delete task", "Task for deleting", true);
        when(remoteDataSource.delete(dTask)).thenReturn(Single.just(dTask));
        when(localDataSource.delete(dTask)).thenReturn(Single.just(dTask));

        TestObserver<Task> testObserver = new TestObserver<>();
        repository.delete(dTask).subscribe(testObserver);

        verify(remoteDataSource).delete(dTask);
        verify(localDataSource).delete(dTask);

        testObserver.assertValue(dTask);
    }

    private void setTaskAvailable(TaskDataSource dataSource, Task task) {
        // don't allow the data sources to complete.
        when(dataSource.task(1)).thenReturn(Maybe.just(task));
    }

    private void setTaskNotAvailable(TaskDataSource dataSource) {
        // don't allow the data sources to complete.
        when(dataSource.task(1)).thenReturn(Maybe.empty());
    }

    private void setTasksAvailable(TaskDataSource dataSource, boolean forceUpdate, List<Task> tasks) {
        // don't allow the data sources to complete.
        when(dataSource.tasks(forceUpdate)).thenReturn(Observable.just(tasks).concatWith(Observable.never()));
    }

    private void setTasksNotAvailable(TaskDataSource dataSource, boolean forceUpdate) {
        when(dataSource.tasks(forceUpdate)).thenReturn(Observable.just(Collections.emptyList()));
    }
}
