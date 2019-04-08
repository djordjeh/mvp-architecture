package djordjeh.architecture.mvp.ui.tasks;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import djordjeh.architecture.mvp.util.SwipeToDeleteCallback;

public class TasksFragment extends BaseFragmentImpl<TasksContract.Presenter> implements TasksContract.View {

    static final String TAG = "TasksFragment";
    static TasksFragment newInstance() {
        return new TasksFragment();
    }

    private FragmentTasksBinding binding;
    private TasksAdapter adapter = new TasksAdapter();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        binding.setPresenter(getPresenter());
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
        getPresenter().getTasks(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.adapter.setListener(getPresenter());
        this.binding.refreshLayout.setOnRefreshListener(getPresenter());
    }

    @Override
    public void onPause() {
        this.adapter.setListener(null);
        this.binding.refreshLayout.setOnRefreshListener(null);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        adapter.unregisterAdapterDataObserver(observer);
        binding.refreshLayout.setOnRefreshListener(null);
        super.onDestroyView();
    }

    @Override
    public void showTasks(List<Task> tasks) {
        adapter.setList(tasks);
    }

    @Override
    public void showTask(@Nullable Task task) {
        TaskFragment.newInstance(task).show(getChildFragmentManager(), TaskFragment.TAG);
    }

    @Override
    public void showError(Throwable throwable) {
        // I've decide to go with simple error handling
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress(boolean show) {
        binding.refreshLayout.setRefreshing(show);
    }

    @Override
    public void showSavedMessage(@NonNull Task task) {
        // Simple Feedback message
        Toast.makeText(getContext(), task.getCompleted() ? getString(R.string.task_completed, task.getTitle()) : getString(R.string.task_not_completed, task.getTitle()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showTaskDeletedMessage(Task task) {
        Snackbar.make(binding.getRoot(), R.string.task_deleted, Snackbar.LENGTH_LONG).setAction(R.string.undo, v -> getPresenter().saveTask(task)).show();
    }

    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            binding.textEmptyList.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
        }
    };
}
