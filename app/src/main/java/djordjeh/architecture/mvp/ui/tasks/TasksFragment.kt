package djordjeh.architecture.mvp.ui.tasks

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import djordjeh.architecture.mvp.R
import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.databinding.FragmentTasksBinding
import djordjeh.architecture.mvp.ui.BaseFragmentImpl
import djordjeh.architecture.mvp.ui.task.TaskFragment
import djordjeh.architecture.mvp.util.SwipeToDeleteCallback

class TasksFragment : BaseFragmentImpl<TasksContract.Presenter>(), TasksContract.View {

    private lateinit var binding: FragmentTasksBinding
    private val adapter = TasksAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            presenter = this@TasksFragment.presenter
            recyclerTasks.adapter = adapter
        }
        ItemTouchHelper(object : SwipeToDeleteCallback(ContextCompat.getDrawable(context!!, R.drawable.ic_delete_24dp)!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                adapter.removeTask(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.recyclerTasks)
        adapter.registerAdapterDataObserver(observer)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.getTasks(false)
    }

    override fun onResume() {
        super.onResume()
        this.adapter.listener = presenter
        this.binding.refreshLayout.setOnRefreshListener(presenter)
    }

    override fun onPause() {
        this.adapter.listener = null
        this.binding.refreshLayout.setOnRefreshListener(null)
        super.onPause()
    }

    override fun onDestroyView() {
        adapter.unregisterAdapterDataObserver(observer)
        binding.refreshLayout.setOnRefreshListener(null)
        super.onDestroyView()
    }

    override fun showTasks(tasks: List<Task>) {
        adapter.list = tasks.toMutableList()
    }

    override fun showTask(task: Task?) {
        TaskFragment.newInstance(task).show(childFragmentManager, TaskFragment.TAG)
    }

    override fun showError(throwable: Throwable) {
        // I've decide to go with simple error handling
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress(show: Boolean) {
        binding.refreshLayout.isRefreshing = show
    }

    override fun showSavedMessage(task: Task) {
        // Simple Feedback message
        Toast.makeText(context, if (task.completed) getString(R.string.task_completed, task.title) else getString(R.string.task_not_completed, task.title), Toast.LENGTH_SHORT).show()
    }

    override fun showTaskDeletedMessage(task: Task) {
        Snackbar.make(binding.root, R.string.task_deleted, Snackbar.LENGTH_LONG).setAction(R.string.undo) { presenter.saveTask(task) }.show()
    }

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            binding.textEmptyList.visibility = if (adapter.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    companion object {
        internal const val TAG = "TasksFragment"
        internal fun newInstance() = TasksFragment()
    }
}
