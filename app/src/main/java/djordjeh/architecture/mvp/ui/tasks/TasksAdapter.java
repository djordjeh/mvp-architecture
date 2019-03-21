package djordjeh.architecture.mvp.ui.tasks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.databinding.ItemTaskBinding;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {

    interface Listener {
        void showTask(Task task);
        void saveTask(Task task);
        void deleteTask(Task task);
    }

    private Listener listener;
    private List<Task> list = new ArrayList<>();

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        taskViewHolder.bindTask(list.get(position));

        taskViewHolder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.showTask(list.get(taskViewHolder.getAdapterPosition()));
            }
        });

        taskViewHolder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                final Task task = list.get(taskViewHolder.getAdapterPosition());
                // breaks possible infinite loops
                if (task.isCompleted() != isChecked) {
                    task.setCompleted(isChecked);
                    listener.saveTask(task);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    boolean isEmpty() {
        return list.isEmpty();
    }

    void setList(List<Task> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    void removeTask(int index) {
        listener.deleteTask(list.get(index));
        list.remove(index);
        notifyItemRemoved(index);
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        private ItemTaskBinding binding;

        TaskViewHolder(@NonNull ItemTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindTask(Task task) {
            binding.setTask(task);
            binding.executePendingBindings();
        }
    }
}
