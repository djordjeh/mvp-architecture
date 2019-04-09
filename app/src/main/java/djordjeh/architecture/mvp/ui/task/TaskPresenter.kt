package djordjeh.architecture.mvp.ui.task

import javax.inject.Inject

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.ui.BasePresenterImpl
import djordjeh.architecture.mvp.ui.SchedulersFacade

class TaskPresenter @Inject
internal constructor(
        view: ContractTask.View,
        private val taskDataSource: TaskDataSource,
        private val schedulersFacade: SchedulersFacade) : BasePresenterImpl<ContractTask.View>(view), ContractTask.Presenter {

    override fun getTask(taskId: Long) {
        if (taskId > -1) {
            addDisposable(taskDataSource.task(taskId)
                    .subscribeOn(schedulersFacade.io())
                    .observeOn(schedulersFacade.mainThread())
                    .subscribe({ view.showTask(it) }, { view.showError(it) })
            )
        } else
            view.showTask(Task())
    }

    override fun saveTask(task: Task) {
        if (isEmpty(task.title)) {
            view.showEmptyTitleError(true)
        } else {
            view.showEmptyTitleError(false)
            addDisposable(taskDataSource.save(task)
                    .subscribeOn(schedulersFacade.io())
                    .observeOn(schedulersFacade.mainThread())
                    .subscribe({ view.onTaskSaved(it) }, { view.showError(it) }))
        }
    }

    private fun isEmpty(str: String?) = str?.isEmpty() ?: true
}
