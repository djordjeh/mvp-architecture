package djordjeh.architecture.mvp.data;

import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
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
    public Observable<List<Task>> tasks() {
        return localDataSource.tasks();
    }

    @Override
    public Single<Boolean> save(Task task) {
        return localDataSource.save(task);
    }

    @Override
    public Single<Task> delete(Task task) {
        return localDataSource.delete(task);
    }
}
