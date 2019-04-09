package djordjeh.architecture.mvp.ui.task

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.ui.SchedulersFacade
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

import org.mockito.Mockito.verify

class TaskDetailsPresenterTest {

    @Mock
    private lateinit var view: ContractTask.View

    @Mock
    private lateinit var taskDataSource: TaskDataSource

    @Mock
    private lateinit var schedulersFacade: SchedulersFacade

    @InjectMocks
    private lateinit var presenter: TaskPresenter

    @Before
    fun setupPresenterTask() {
        MockitoAnnotations.initMocks(this)

        // SchedulersFacade
        Mockito.doReturn(Schedulers.trampoline()).`when`<SchedulersFacade>(schedulersFacade).io()
        Mockito.doReturn(Schedulers.trampoline()).`when`<SchedulersFacade>(schedulersFacade).mainThread()
    }

    @Test
    fun getTaskAndShowIntoView() {
        Mockito.doReturn(Maybe.just(TASK)).`when`<TaskDataSource>(taskDataSource).task(1)
        presenter.getTask(1)
        verify(taskDataSource).task(1)
        verify(view).showTask(TASK)
    }

    @Test
    fun getTaskWithError() {
        val throwable = Throwable("Task not found")
        Mockito.doReturn(Maybe.error<Throwable>(throwable)).`when`<TaskDataSource>(taskDataSource).task(1)
        presenter.getTask(1)
        verify(view).showError(throwable)
    }

    @Test
    fun saveTaskWithEmptyTitle() {
        val task = Task()
        presenter.saveTask(task)
        verify(taskDataSource, Mockito.never()).save(task)
        verify(view).showEmptyTitleError(true)
    }

    @Test
    fun saveTaskAndShowIntoView() {
        Mockito.doReturn(Single.just(TASK)).`when`<TaskDataSource>(taskDataSource).save(TASK)
        presenter.saveTask(TASK)
        verify(taskDataSource).save(TASK)
        verify(view).showEmptyTitleError(false)
        verify(view).onTaskSaved(TASK)
    }

    @Test
    fun saveTaskWithErrorAndShotIntoView() {
        val throwable = Throwable("No Internet connection")
        Mockito.doReturn(Single.error<Throwable>(throwable)).`when`<TaskDataSource>(taskDataSource).save(TASK)
        presenter.saveTask(TASK)
        verify(view).showError(throwable)
    }

    @After
    fun destroy() {
        presenter.destroy()
    }

    companion object {
        private val TASK = Task(1, "Title", "Description", false)
    }
}
