package djordjeh.architecture.mvp.dagger;

import java.util.List;

import javax.inject.Singleton;

import androidx.annotation.NonNull;
import dagger.Module;
import dagger.Provides;
import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.ui.SchedulersFacade;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@Module
public class TestApplicationModule {

    private TaskDataSource taskDataSource;

    public TestApplicationModule(TaskDataSource taskDataSource) {
        this.taskDataSource = taskDataSource;
    }

    @Provides
    @Singleton
    TaskDataSource taskRepository() {
        return new MockTasksDataSource(taskDataSource);
    }

    @Provides
    @Singleton
    SchedulersFacade provideSchedulersFacade() {
        return new TestSchedulersFacadeImpl();
    }

    static class TestSchedulersFacadeImpl implements SchedulersFacade {

        @Override
        public Scheduler io() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler computation() {
            return Schedulers.trampoline();
        }

        @Override
        public Scheduler mainThread() {
            return Schedulers.trampoline();
        }
    }

    static class MockTasksDataSource implements TaskDataSource {

        private TaskDataSource taskDataSource;

        MockTasksDataSource(TaskDataSource taskDataSource) {
            this.taskDataSource = taskDataSource;
        }

        @Override
        public Observable<List<Task>> tasks(boolean forceUpdate) {
            return taskDataSource.tasks(forceUpdate);
        }

        @Override
        public Maybe<Task> task(long taskId) {
            return taskDataSource.task(taskId);
        }

        @Override
        public Single<Task> save(@NonNull Task task) {
            return Single.just(task);
        }

        @Override
        public Single<Task> delete(@NonNull Task task) {
            return Single.just(task);
        }
    }
}
