package djordjeh.architecture.mvp.ui.tasks

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

import java.util.Arrays

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.ui.SchedulersFacade
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class TasksPresenterTest {

    @Mock
    private lateinit var view: TasksContract.View

    @Mock
    private lateinit var taskDataSource: TaskDataSource

    @Mock
    private lateinit var schedulersFacade: SchedulersFacade

    @InjectMocks
    private lateinit var presenter: TasksPresenter

    @Before
    fun setupPresenterTasks() {
        MockitoAnnotations.initMocks(this)

        // SchedulersFacade
        Mockito.doReturn(Schedulers.trampoline()).`when`<SchedulersFacade>(schedulersFacade).io()
        Mockito.doReturn(Schedulers.trampoline()).`when`<SchedulersFacade>(schedulersFacade).mainThread()
    }

    @Test
    fun loadTasksFromRemoteDataSourceAndLoadIntoView() {
        val forceUpdate = true
        Mockito.doReturn(Observable.just(tasksResult)).`when`(taskDataSource).tasks(forceUpdate)
        presenter.getTasks(forceUpdate)
        verify(taskDataSource).tasks(forceUpdate)

        val inOrder = Mockito.inOrder(view)
        inOrder.verify(view).showTasks(tasksResult)
        inOrder.verify(view).showProgress(false)
    }

    @Test
    fun observeTasksLocallyAndLoadIntoView() {
        val forceUpdate = false
        Mockito.doReturn(Observable.just(tasksResult)).`when`(taskDataSource).tasks(forceUpdate)
        presenter.getTasks(forceUpdate)
        verify(taskDataSource).tasks(forceUpdate)

        verify(view, never()).showProgress(true)
        verify(view).showTasks(tasksResult)
    }

    @Test
    fun showLoadTasksError() {
        Mockito.doReturn(Observable.error<Any>(throwable)).`when`(taskDataSource).tasks(true)
        presenter.getTasks(true)
        verify(view).showError(throwable)
    }

    @Test
    fun saveTaskInRepoAndShowIntoView() {
        Mockito.doReturn(Single.just(task)).`when`(taskDataSource).save(task)
        presenter.saveTask(task)
        verify(taskDataSource).save(task)
        verify(view).showSavedMessage(task)
    }

    @Test
    fun saveTaskWithError() {
        Mockito.doReturn(Single.error<Any>(throwable)).`when`(taskDataSource).save(task)
        presenter.saveTask(task)
        verify(view).showError(throwable)
    }

    @Test
    fun deleteTaskAndShowMessageIntoView() {
        Mockito.doReturn(Single.just(task)).`when`(taskDataSource).delete(task)
        presenter.deleteTask(task)
        verify(taskDataSource).delete(task)
        verify(view).showTaskDeletedMessage(task)
    }

    @Test
    fun deleteTaskWithError() {
        Mockito.doReturn(Single.error<Any>(throwable)).`when`(taskDataSource).delete(task)
        presenter.deleteTask(task)
        verify(view).showError(throwable)
    }

    @Test
    fun addTaskShowIntoView() {
        presenter.addNewTask()
        verify(view).showTask(null)
    }

    @Test
    fun showTaskIntoView() {
        presenter.showTask(task)
        verify(view).showTask(task)
    }

    @Test
    fun refreshTasksAndShowIntoView() {
        Mockito.doReturn(Observable.just(tasksResult)).`when`(taskDataSource).tasks(true)
        presenter.onRefresh()
        verify(taskDataSource).tasks(true)
    }

    @After
    fun destroy() {
        presenter.destroy()
    }

    companion object {

        private val throwable = Throwable("No Internet connection")
        private val task = Task(1, "Title", "Description", false)
        private val task2 = Task(2, "Title2", "Description2", true)
        private val tasksResult = Arrays.asList(task, task2)
    }
}
