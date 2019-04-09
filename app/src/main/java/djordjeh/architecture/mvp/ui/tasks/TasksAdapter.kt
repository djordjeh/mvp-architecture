package djordjeh.architecture.mvp.ui.tasks

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.databinding.ItemTaskBinding

class TasksAdapter : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    var listener: Listener? = null
    var list = mutableListOf<Task>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    fun isEmpty() = list.isEmpty()

    interface Listener {
        fun showTask(task: Task)
        fun saveTask(task: Task)
        fun deleteTask(task: Task)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false))
    }

    override fun onBindViewHolder(taskViewHolder: TaskViewHolder, position: Int) {
        taskViewHolder.bindTask(list[position])

        taskViewHolder.itemView.setOnClickListener {
            listener?.showTask(list[taskViewHolder.adapterPosition])
        }

        taskViewHolder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val task = list[taskViewHolder.adapterPosition]
            // breaks possible infinite loops
            if (task.completed != isChecked) {
                task.completed = isChecked
                listener?.saveTask(task)
            }
        }
    }

    override fun getItemCount() = list.size

    fun removeTask(index: Int) {
        listener?.deleteTask(list[index])
        list.removeAt(index)
        notifyItemRemoved(index)
    }

    class TaskViewHolder(var binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindTask(task: Task) {
            binding.task = task
            binding.executePendingBindings()
        }
    }
}
