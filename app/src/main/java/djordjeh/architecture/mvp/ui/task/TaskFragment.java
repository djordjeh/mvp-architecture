package djordjeh.architecture.mvp.ui.task;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
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
import djordjeh.architecture.mvp.ui.util.TextWatcherHelper;

public class TaskFragment extends BottomSheetDialogFragment implements ContractTask.View {

    public static final String TAG = "TaskFragment";
    private static final String ARG_TASK = "arg_task";

    public static TaskFragment newInstance(@Nullable Task task) {
        Bundle args = new Bundle();
        // We need a new instance of the task
        args.putParcelable(ARG_TASK, task != null ? new Task(task.getId(), task.getTitle(), task.getDescription(), task.isCompleted()) : new Task());
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    ContractTask.Presenter presenter;

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
        binding.setTask(getArguments().getParcelable(ARG_TASK));
        binding.editTextTitle.addTextChangedListener(titleWatcher);
    }

    @Override
    public void onDestroyView() {
        binding.editTextTitle.removeTextChangedListener(titleWatcher);
        super.onDestroyView();
    }

    @Override
    public void showEmptyTitleError(boolean show) {
        binding.inputTitle.setErrorEnabled(show);
        binding.inputTitle.setError(show ? getString(R.string.error_empty_title) : null);
    }

    @Override
    public void onTaskSaved(boolean success) {
        Toast.makeText(getContext(), success ? R.string.task_saved : R.string.task_save_error, Toast.LENGTH_SHORT).show();
        if (success) {
            dismiss();
        }
    }

    @Override
    public void showError(Throwable throwable) {
        Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private TextWatcherHelper titleWatcher = new TextWatcherHelper() {
        @Override
        public void afterTextChanged(Editable s) {
            showEmptyTitleError(TextUtils.isEmpty(s));
        }
    };
}
