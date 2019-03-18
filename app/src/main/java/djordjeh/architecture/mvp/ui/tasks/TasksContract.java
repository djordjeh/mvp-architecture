package djordjeh.architecture.mvp.ui.tasks;

import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.ui.BasePresenter;

public interface TasksContract {

    interface View {
        void showTasks(List<Task> tasks);
        void showError(Throwable throwable);
        void showSavedMessage(boolean success);
        void showTaskDeletedMessage(Task task);
    }

    interface Presenter extends BasePresenter {
        void save(Task task);
        void delete(Task task);
    }
}
