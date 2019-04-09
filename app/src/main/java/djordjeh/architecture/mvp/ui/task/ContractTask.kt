package djordjeh.architecture.mvp.ui.task


import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.ui.BasePresenter

interface ContractTask {

    interface View {
        fun showTask(task: Task)
        fun showEmptyTitleError(show: Boolean)
        fun onTaskSaved(task: Task)
        fun showError(throwable: Throwable)
    }

    interface Presenter : BasePresenter {
        fun getTask(taskId: Long)
        fun saveTask(task: Task)
    }
}
