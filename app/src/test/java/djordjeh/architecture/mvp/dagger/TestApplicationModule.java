package djordjeh.architecture.mvp.dagger;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Singleton;

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
class TestApplicationModule {

    @Provides
    @Singleton
    TaskDataSource taskRepository() {
        return new MockTaskRemoteDataSource();
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

    static class MockTaskRemoteDataSource implements TaskDataSource {

        @Override
        public Observable<List<Task>> tasks(boolean forceUpdate) {

            return Observable.empty();
        }

        @Override
        public Maybe<Task> task(long taskId) {
            return Maybe.empty();
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
