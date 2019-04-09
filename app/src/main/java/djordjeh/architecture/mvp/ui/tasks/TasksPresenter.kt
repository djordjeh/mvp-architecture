package djordjeh.architecture.mvp.ui.tasks

import javax.inject.Inject

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.ui.BasePresenterImpl
import djordjeh.architecture.mvp.ui.SchedulersFacade

class TasksPresenter @Inject
internal constructor(
        view: TasksContract.View,
        private val taskDataSource: TaskDataSource,
        private val schedulersFacade: SchedulersFacade) : BasePresenterImpl<TasksContract.View>(view), TasksContract.Presenter {

    override fun getTasks(forceUpdate: Boolean) {
        addDisposable(
                taskDataSource.tasks(forceUpdate)
                        .subscribeOn(schedulersFacade.io())
                        .observeOn(schedulersFacade.mainThread())
                        .subscribe( { view.showTasks(it) }, { view.showError(it) }, { view.showProgress(false) })
        )
    }

    override fun saveTask(task: Task) {
        addDisposable(taskDataSource.save(task)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.mainThread())
                .subscribe({ view.showSavedMessage(it) }, { view.showError(it) }))
    }

    override fun deleteTask(task: Task) {
        addDisposable(taskDataSource.delete(task)
                .subscribeOn(schedulersFacade.io())
                .observeOn(schedulersFacade.mainThread())
                .subscribe({ view.showTaskDeletedMessage(it) }, { view.showError(it) }))
    }

    override fun addNewTask() {
        view.showTask(null)
    }

    override fun showTask(task: Task) {
        view.showTask(task)
    }

    override fun onRefresh() {
        getTasks(true)
    }
}
