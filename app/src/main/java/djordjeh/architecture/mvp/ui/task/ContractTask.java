package djordjeh.architecture.mvp.ui.task;


import androidx.annotation.NonNull;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.ui.BasePresenter;

public interface ContractTask {

    interface View {
        void showTask(@NonNull Task task);
        void showEmptyTitleError(boolean show);
        void onTaskSaved(Task task);
        void showError(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void getTask(long taskId);
        void saveTask(Task task);
    }
}
