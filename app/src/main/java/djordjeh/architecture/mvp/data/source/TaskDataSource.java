package djordjeh.architecture.mvp.data.source;

import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface TaskDataSource {
    Observable<List<Task>> tasks();
    Single<Boolean> save(Task task);
    Single<Task> delete(Task task);
}
