package djordjeh.architecture.mvp.ui.task;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import djordjeh.architecture.mvp.R;
import djordjeh.architecture.mvp.data.model.Task;
import djordjeh.architecture.mvp.databinding.FragmentTaskBinding;
import djordjeh.architecture.mvp.util.TextWatcherHelper;

public class TaskFragment extends BottomSheetDialogFragment implements ContractTask.View {

    public static final String TAG = "TaskFragment";
    private static final String ARG_TASK = "arg_task";
    private static final String ARG_TASK_ID = "arg_task_id";

    public static TaskFragment newInstance(@Nullable Task task) {
        Bundle args = new Bundle();
        if (task != null) {
            args.putLong(ARG_TASK_ID, task.getId());
        }
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    public ContractTask.Presenter presenter;

    private FragmentTaskBinding binding;

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTaskBinding.inflate(inflater, container, false);
        binding.setPresenter(presenter);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.editTextTitle.addTextChangedListener(titleWatcher);

        if (savedInstanceState != null && savedInstanceState.containsKey(ARG_TASK)) {
            binding.setTask(savedInstanceState.getParcelable(ARG_TASK));
        } else {
            presenter.getTask(getArguments() != null ? getArguments().getLong(ARG_TASK_ID, -1) : -1);
        }
    }

    @Override
    public void onDestroyView() {
        binding.editTextTitle.removeTextChangedListener(titleWatcher);
        super.onDestroyView();
    }

    @Override
    public void showTask(@NonNull Task task) {
        binding.setTask(task);
    }

    @Override
    public void showEmptyTitleError(boolean show) {
        binding.inputTitle.setErrorEnabled(show);
        binding.inputTitle.setError(show ? getString(R.string.error_empty_title) : null);
    }

    @Override
    public void onTaskSaved(@NonNull Task task) {
        Toast.makeText(getContext(), getString(R.string.task_saved, task.getTitle()), Toast.LENGTH_SHORT).show();
        dismiss();
    }

    @Override
    public void showError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        final Task task = binding.getTask();
        if (task != null) {
            outState.putParcelable(ARG_TASK, task);
        }
        super.onSaveInstanceState(outState);
    }

    private TextWatcherHelper titleWatcher = new TextWatcherHelper() {
        @Override
        public void afterTextChanged(Editable s) {
            showEmptyTitleError(TextUtils.isEmpty(s));
        }
    };
}
