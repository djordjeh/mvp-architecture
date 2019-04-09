package djordjeh.architecture.mvp.ui.task

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import javax.inject.Inject

import dagger.android.support.AndroidSupportInjection
import djordjeh.architecture.mvp.R
import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.databinding.FragmentTaskBinding
import djordjeh.architecture.mvp.util.TextWatcherHelper

class TaskFragment : BottomSheetDialogFragment(), ContractTask.View {

    @Inject
    lateinit var presenter: ContractTask.Presenter

    lateinit var binding: FragmentTaskBinding

    private val titleWatcher = object : TextWatcherHelper() {
        override fun afterTextChanged(s: Editable) {
            showEmptyTitleError(TextUtils.isEmpty(s))
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskBinding.inflate(inflater, container, false).apply {
            presenter = this@TaskFragment.presenter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editTextTitle.addTextChangedListener(titleWatcher)

        if (savedInstanceState?.containsKey(ARG_TASK) == true) {
            binding.task = savedInstanceState.getParcelable(ARG_TASK)
        } else {
            presenter.getTask(arguments?.getLong(ARG_TASK_ID, -1) ?: -1)
        }
    }

    override fun onDestroyView() {
        binding.editTextTitle.removeTextChangedListener(titleWatcher)
        super.onDestroyView()
    }

    override fun showTask(task: Task) {
        binding.task = task
    }

    override fun showEmptyTitleError(show: Boolean) {
        binding.inputTitle.isErrorEnabled = show
        binding.inputTitle.error = if (show) getString(R.string.error_empty_title) else null
    }

    override fun onTaskSaved(task: Task) {
        Toast.makeText(context, getString(R.string.task_saved, task.title), Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun showError(throwable: Throwable) {
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.task?.let { task->
            outState.putParcelable(ARG_TASK, task)
        }
        super.onSaveInstanceState(outState)
    }

    companion object {

        const val TAG = "TaskFragment"
        private const val ARG_TASK = "arg_task"
        private const val ARG_TASK_ID = "arg_task_id"

        fun newInstance(task: Task?): TaskFragment {
            return TaskFragment().apply {
                arguments = Bundle().apply {
                    task?.let {
                        putLong(ARG_TASK_ID, it.id)
                    }
                }
            }
        }
    }


}
