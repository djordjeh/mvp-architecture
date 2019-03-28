package djordjeh.architecture.mvp.data;

import android.support.annotation.NonNull;

import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class MockTaskRepository implements TaskDataSource {

    @Override
    public Observable<List<Task>> tasks(boolean forceUpdate) {
        return null;
    }

    @Override
    public Maybe<Task> task(long taskId) {
        return null;
    }

    @Override
    public Single<Task> save(@NonNull Task task) {
        return null;
    }

    @Override
    public Single<Task> delete(@NonNull Task task) {
        return null;
    }
}
