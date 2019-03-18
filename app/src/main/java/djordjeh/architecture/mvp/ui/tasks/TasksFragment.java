package djordjeh.architecture.mvp.ui.tasks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import djordjeh.architecture.mvp.R;
import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.databinding.FragmentTasksBinding;
import djordjeh.architecture.mvp.ui.BaseFragmentImpl;
import djordjeh.architecture.mvp.ui.task.TaskFragment;
import djordjeh.architecture.mvp.ui.util.SwipeToDeleteCallback;

public class TasksFragment extends BaseFragmentImpl<TasksContract.Presenter> implements TasksContract.View, TasksAdapter.Listener {

    public static final String TAG = "TasksFragment";
    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    private FragmentTasksBinding binding;
    private TasksAdapter adapter = new TasksAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        binding.recyclerTasks.setAdapter(adapter);
        new ItemTouchHelper(new SwipeToDeleteCallback(ContextCompat.getDrawable(getContext(), R.drawable.ic_delete_24dp)){
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                adapter.removeTask(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(binding.recyclerTasks);
        adapter.registerAdapterDataObserver(observer);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adapter.setListener(this);
        this.binding.buttonAdd.setOnClickListener(view -> {
            TaskFragment.newInstance(null).show(getChildFragmentManager(), TaskFragment.TAG);
        });
    }

    @Override
    public void onPause() {
        this.adapter.setListener(null);
        this.binding.buttonAdd.setOnClickListener(null);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        adapter.unregisterAdapterDataObserver(observer);
        super.onDestroyView();
    }

    @Override
    public void showTasks(List<Task> tasks) {
        adapter.setList(tasks);
    }

    @Override
    public void showError(Throwable throwable) {
        // I've decide to go with simple error handling
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSavedMessage(boolean success) {
        // Simple Feedback message
        Toast.makeText(getContext(), success ? R.string.task_saved : R.string.task_save_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTaskDeletedMessage(Task task) {
        Snackbar.make(binding.getRoot(), R.string.task_deleted, Snackbar.LENGTH_LONG).setAction(R.string.undo, v -> {
            presenter.save(task);
        }).show();
    }

    @Override
    public void onTaskSelected(Task task) {
        TaskFragment.newInstance(task).show(getChildFragmentManager(), TaskFragment.TAG);
    }

    @Override
    public void onTaskCheckedChanged(Task task) {
        presenter.save(task);
    }

    @Override
    public void onTaskDeleted(Task task) {
        presenter.delete(task);
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            binding.textEmptyList.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
        }
    };
}
