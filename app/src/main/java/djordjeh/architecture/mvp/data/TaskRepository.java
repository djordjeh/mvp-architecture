package djordjeh.architecture.mvp.data;

import android.support.annotation.NonNull;

import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class TaskRepository implements TaskDataSource {

    private final TaskDataSource localDataSource;
    private final TaskDataSource remoteDataSource;

    TaskRepository(TaskDataSource localDataSource, TaskDataSource remoteDataSource) {
        this.localDataSource = localDataSource;
        this.remoteDataSource = remoteDataSource;
    }

    @Override
    public Observable<List<Task>> tasks(boolean forceUpdate) {
        if (forceUpdate) return remoteDataSource.tasks(true)
                .flatMap(tasks -> Observable.fromIterable(tasks)
                        .doOnNext(localDataSource::save)
                        .toList()
                        .toObservable());
        else return localDataSource.tasks(false);
    }

    @Override
    public Maybe<Task> task(long taskId) {
        return Maybe.concat(localDataSource.task(taskId), remoteDataSource.task(taskId)).firstElement();
    }

    @Override
    public Single<Task> save(@NonNull Task task) {
        return remoteDataSource.save(task)
                .flatMap(localDataSource::save);
    }

    @Override
    public Single<Task> delete(@NonNull Task task) {
        return remoteDataSource.delete(task)
                .flatMap(localDataSource::delete);
    }
}
