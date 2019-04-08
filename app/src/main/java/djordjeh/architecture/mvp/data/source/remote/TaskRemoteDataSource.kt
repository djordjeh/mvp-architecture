package djordjeh.architecture.mvp.data.source.remote

import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.data.model.Task
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

// I've decided to use mocked remote data source
class TaskRemoteDataSource : TaskDataSource {

    override fun tasks(forceUpdate: Boolean): Observable<List<Task>> {
        return Observable.just(TASKS)
    }

    override fun task(taskId: Long): Maybe<Task> {
        return Maybe.just(TASK)
    }

    override fun save(task: Task): Single<Task> {
        return Single.just(task)
    }

    override fun delete(task: Task): Single<Task> {
        return Single.just(task)
    }

    companion object {

        private val TASK = Task(1, "Remote Task", "This is task from remote server", false)
        private val TASKS = arrayListOf(TASK)
    }
}
