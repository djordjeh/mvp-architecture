package djordjeh.architecture.mvp.data.source.remote;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.data.model.Task;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class TaskRemoteDataSource implements TaskDataSource {

    private static final Task TASK = new Task(1, "Remote Task", "This is task from remote server", false);
    private static final List<Task> TASKS = new ArrayList<Task>() {{
        add(TASK);
    }};

    public TaskRemoteDataSource() {
    }

    @Override
    public Observable<List<Task>> tasks(boolean forceUpdate) {
        return Observable.just(TASKS);
    }

    @Override
    public Maybe<Task> task(long taskId) {
        return Maybe.just(TASK);
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
