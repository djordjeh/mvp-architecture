package djordjeh.architecture.mvp.data.source

import djordjeh.architecture.mvp.data.model.Task
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface TaskDataSource {
    fun tasks(forceUpdate: Boolean): Observable<List<Task>>
    fun task(taskId: Long): Maybe<Task>
    fun save(task: Task): Single<Task>
    fun delete(task: Task): Single<Task>
}
