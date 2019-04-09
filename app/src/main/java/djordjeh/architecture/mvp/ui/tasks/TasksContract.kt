package djordjeh.architecture.mvp.ui.tasks

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.ui.BasePresenter

interface TasksContract {

    interface View {
        fun showTasks(tasks: List<Task>)
        fun showTask(task: Task?)
        fun showError(throwable: Throwable)
        fun showProgress(show: Boolean)
        fun showSavedMessage(task: Task)
        fun showTaskDeletedMessage(task: Task)
    }

    interface Presenter : BasePresenter, TasksAdapter.Listener, SwipeRefreshLayout.OnRefreshListener {
        fun getTasks(forceUpdate: Boolean)
        fun addNewTask()
    }
}
