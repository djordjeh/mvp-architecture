package djordjeh.architecture.mvp.ui.task;


import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.ui.BasePresenter;

public interface ContractTask {

    interface View {
        void showEmptyTitleError(boolean show);
        void onTaskSaved(boolean success);
        void showError(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void save(Task task);
    }
}
