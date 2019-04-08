package djordjeh.architecture.mvp.ui.tasks;

import javax.inject.Inject;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.ui.BasePresenterImpl;
import djordjeh.architecture.mvp.ui.SchedulersFacade;

public class TasksPresenter extends BasePresenterImpl<TasksContract.View> implements TasksContract.Presenter {

    private final TaskDataSource taskDataSource;
    private final SchedulersFacade schedulersFacade;

    @Inject
    TasksPresenter(TasksContract.View view, TaskDataSource taskDataSource, SchedulersFacade schedulersFacade) {
        super(view);
        this.taskDataSource = taskDataSource;
        this.schedulersFacade = schedulersFacade;
    }

    @Override
    public void getTasks(boolean forceUpdate) {
        addDisposable(
                taskDataSource.tasks(forceUpdate)
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.mainThread())
                        .subscribe(getView()::showTasks, getView()::showError, () -> getView().showProgress(false))
        );
    }

    @Override
    public void saveTask(Task task) {
        addDisposable(taskDataSource.save(task)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.mainThread())
                .subscribe(getView()::showSavedMessage, getView()::showError));
    }

    @Override
    public void deleteTask(Task task) {
        addDisposable(taskDataSource.delete(task)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.mainThread())
                .subscribe(getView()::showTaskDeletedMessage, getView()::showError));
    }

    @Override
    public void addNewTask() {
        getView().showTask(null);
    }

    @Override
    public void showTask(Task task) {
        getView().showTask(task);
    }

    @Override
    public void onRefresh() {
        getTasks(true);
    }
}
