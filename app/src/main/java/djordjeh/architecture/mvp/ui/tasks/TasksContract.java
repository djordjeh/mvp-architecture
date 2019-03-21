package djordjeh.architecture.mvp.ui.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.ui.BasePresenter;

public interface TasksContract {

    interface View {
        void showTasks(List<Task> tasks);
        void showTask(@Nullable Task task);
        void showError(Throwable throwable);
        void showProgress(boolean show);
        void showSavedMessage(@NonNull Task task);
        void showTaskDeletedMessage(Task task);
    }

    interface Presenter extends BasePresenter, TasksAdapter.Listener {
        void getTasks(boolean forceUpdate);
        void addNewTask();
    }
}
