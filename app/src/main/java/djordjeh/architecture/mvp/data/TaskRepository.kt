package djordjeh.architecture.mvp.data

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class TaskRepository internal constructor(private val localDataSource: TaskDataSource, private val remoteDataSource: TaskDataSource) : TaskDataSource {

    override fun tasks(forceUpdate: Boolean): Observable<List<Task>> {
        return if (forceUpdate)
            remoteDataSource.tasks(true)
                    .flatMap { tasks ->
                        Observable.fromIterable(tasks)
                                .doOnNext { task: Task -> localDataSource.save(task) }
                                .toList()
                                .toObservable()
                    }
        else
            localDataSource.tasks(false)
    }

    override fun task(taskId: Long): Maybe<Task> {
        return Maybe.concat(localDataSource.task(taskId), remoteDataSource.task(taskId)).firstElement()
    }

    override fun save(task: Task): Single<Task> {
        return remoteDataSource.save(task)
                .flatMap { localDataSource.save(it) }
    }

    override fun delete(task: Task): Single<Task> {
        return remoteDataSource.delete(task)
                .flatMap { localDataSource.delete(it) }
    }
}
