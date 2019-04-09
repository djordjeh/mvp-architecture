package djordjeh.architecture.mvp.dagger

import javax.inject.Singleton
import dagger.Module
import dagger.Provides
import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.ui.SchedulersFacade
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

@Module
class TestApplicationModule(private val taskDataSource: TaskDataSource) {

    @Provides
    @Singleton
    internal fun taskRepository(): TaskDataSource {
        return MockTasksDataSource(taskDataSource)
    }

    @Provides
    @Singleton
    internal fun provideSchedulersFacade(): SchedulersFacade {
        return TestSchedulersFacadeImpl()
    }

    internal class TestSchedulersFacadeImpl : SchedulersFacade {

        override fun io(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun computation(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun mainThread(): Scheduler {
            return Schedulers.trampoline()
        }
    }

    internal class MockTasksDataSource(private val taskDataSource: TaskDataSource) : TaskDataSource {

        override fun tasks(forceUpdate: Boolean): Observable<List<Task>> {
            return taskDataSource.tasks(forceUpdate)
        }

        override fun task(taskId: Long): Maybe<Task> {
            return taskDataSource.task(taskId)
        }

        override fun save(task: Task): Single<Task> {
            return Single.just(task)
        }

        override fun delete(task: Task): Single<Task> {
            return Single.just(task)
        }
    }
}
