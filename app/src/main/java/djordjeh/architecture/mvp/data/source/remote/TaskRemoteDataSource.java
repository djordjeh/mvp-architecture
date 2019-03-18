package djordjeh.architecture.mvp.data.source.remote;

import java.util.List;

import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.data.model.Task;
import io.reactivex.Observable;
import io.reactivex.Single;

public class TaskRemoteDataSource implements TaskDataSource {

    public TaskRemoteDataSource() {
    }

    @Override
    public Observable<List<Task>> tasks() {
        return null;
    }

    @Override
    public Single<Boolean> save(Task task) {
        return null;
    }

    @Override
    public Single<Task> delete(Task task) {
        return null;
    }
}
