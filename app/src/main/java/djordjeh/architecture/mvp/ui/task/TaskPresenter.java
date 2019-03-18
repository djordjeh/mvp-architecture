package djordjeh.architecture.mvp.ui.task;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.ui.BasePresenterImpl;
import djordjeh.architecture.mvp.ui.SchedulersFacade;

public class TaskPresenter extends BasePresenterImpl<ContractTask.View> implements ContractTask.Presenter {

    private final TaskDataSource taskDataSource;
    private final SchedulersFacade schedulersFacade;

    @Inject
    TaskPresenter(@NonNull ContractTask.View view, TaskDataSource taskDataSource, SchedulersFacade schedulersFacade) {
        super(view);
        this.taskDataSource = taskDataSource;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    public void save(Task task) {
        if (isEmpty(task.getTitle())) {
            view.showEmptyTitleError(true);
        } else {
            view.showEmptyTitleError(false);
            addDisposable(taskDataSource.save(task)
                    .subscribeOn(schedulersFacade.io())
                    .observeOn(schedulersFacade.mainThread())
                    .subscribe(view::onTaskSaved, view::showError));
        }
    }

    @Override
    public void start() {
        // do nothing
    }

    private static boolean isEmpty(@Nullable String str) {
        return str == null || str.isEmpty();
    }
}
