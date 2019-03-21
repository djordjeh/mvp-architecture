package djordjeh.architecture.mvp.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface TaskDataSource {
    Observable<List<Task>> tasks(boolean forceUpdate);
    Maybe<Task> task(long taskId);
    Single<Task> save(@NonNull Task task);
    Single<Task> delete(@NonNull Task task);
}
