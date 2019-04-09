package djordjeh.architecture.mvp.data

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import java.util.ArrayList

import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver

import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TaskRepositoryTest {

    private lateinit var testObserver: TestObserver<List<Task>>

    @Mock
    private lateinit var localDataSource: TaskDataSource

    @Mock
    private lateinit var remoteDataSource: TaskDataSource

    private lateinit var repository: TaskRepository

    @Before
    fun setupRepository() {
        MockitoAnnotations.initMocks(this)

        repository = TaskRepository(localDataSource, remoteDataSource)

        testObserver = TestObserver()
    }

    @Test
    fun loadTasks__repositoryAfterFirstSubscription_fromRemoteSourceAndSaveThem() {
        val forceUpdate = true
        // We are fetching data from remote data source which has result
        setTasksAvailable(remoteDataSource, forceUpdate, TASKS)

        val testObserver = TestObserver<List<Task>>()
        repository.tasks(forceUpdate).subscribe(testObserver)

        // Then tasks were only requested once from remote and saved by local sources
        verify(remoteDataSource).tasks(forceUpdate)
        verify<TaskDataSource>(localDataSource, never()).tasks(forceUpdate)
        verify<TaskDataSource>(localDataSource).save(task1)
        verify<TaskDataSource>(localDataSource).save(task2)

        testObserver.assertValue(TASKS)
    }

    @Test
    fun loadTasks__repositoryAfterFirstSubscription_fromLocalSource() {
        val forceUpdate = false
        // We are fetching data from local data source which has result
        setTasksAvailable(localDataSource, forceUpdate, TASKS)

        val testObserver = TestObserver<List<Task>>()
        repository.tasks(forceUpdate).subscribe(testObserver)

        verify(localDataSource).tasks(forceUpdate)
        verify<TaskDataSource>(remoteDataSource, never()).tasks(forceUpdate)

        testObserver.assertValue(TASKS)
    }

    @Test
    fun loadTasks_repositoryFromRemoteDataSource() {
        val forceUpdate = true
        // We are fetching data from local data source which has result
        setTasksAvailable(remoteDataSource, forceUpdate, TASKS)

        // When tasks are requested from the tasks repository
        repository.tasks(forceUpdate).subscribe(testObserver)

        // Then tasks are loaded from the local data source
        verify(remoteDataSource).tasks(forceUpdate)
        verify<TaskDataSource>(localDataSource, never()).tasks(forceUpdate)
        testObserver.assertValue(TASKS)
    }

    @Test
    fun loadTasks_repositoryFromLocalDataSource() {
        val forceUpdate = false
        // We are fetching data from local data source which has result
        setTasksAvailable(localDataSource, forceUpdate, TASKS)

        // When tasks are requested from the tasks repository
        repository.tasks(forceUpdate).subscribe(testObserver)

        // Then tasks are loaded from the local data source
        verify(localDataSource).tasks(forceUpdate)
        verify<TaskDataSource>(remoteDataSource, never()).tasks(forceUpdate)
        testObserver.assertValue(TASKS)
    }

    @Test
    fun loadTask_repositoryAfterFirstSubscription_fromLocalSource() {
        setTaskAvailable(localDataSource, task1)
        setTaskAvailable(remoteDataSource, task2)

        val testObserver = TestObserver<Task>()
        repository.task(1).subscribe(testObserver)

        // Then tasks were only requested once from remote and saved by local sources
        verify(localDataSource).task(1)
        verify(remoteDataSource).task(1)

        testObserver.assertValue(task1)
    }

    @Test
    fun loadTask_repositoryAfterFirstSubscription_fromRemoteSource() {
        setTaskNotAvailable(localDataSource)
        setTaskAvailable(remoteDataSource, task2)

        val testObserver = TestObserver<Task>()
        repository.task(1).subscribe(testObserver)

        // Then tasks were only requested once from remote and saved by local sources
        verify(localDataSource).task(1)
        verify(remoteDataSource).task(1)

        testObserver.assertValue(task2)
    }

    @Test
    fun saveTask() {
        val newTask = Task(123, "New task", "new description", false)
        `when`(remoteDataSource.save(newTask)).thenReturn(Single.just(newTask))
        `when`(localDataSource.save(newTask)).thenReturn(Single.just(newTask))

        val testObserver = TestObserver<Task>()
        repository.save(newTask).subscribe(testObserver)

        verify(remoteDataSource).save(newTask)
        verify(localDataSource).save(newTask)

        testObserver.assertValue(newTask)
    }

    @Test
    fun deleteTask() {
        val dTask = Task(321, "Delete task", "Task for deleting", true)
        `when`(remoteDataSource.delete(dTask)).thenReturn(Single.just(dTask))
        `when`(localDataSource.delete(dTask)).thenReturn(Single.just(dTask))

        val testObserver = TestObserver<Task>()
        repository.delete(dTask).subscribe(testObserver)

        verify(remoteDataSource).delete(dTask)
        verify(localDataSource).delete(dTask)

        testObserver.assertValue(dTask)
    }

    private fun setTaskAvailable(dataSource: TaskDataSource, task: Task) {
        // don't allow the data sources to complete.
        `when`(dataSource.task(1)).thenReturn(Maybe.just(task))
    }

    private fun setTaskNotAvailable(dataSource: TaskDataSource) {
        // don't allow the data sources to complete.
        `when`(dataSource.task(1)).thenReturn(Maybe.empty())
    }

    private fun setTasksAvailable(dataSource: TaskDataSource, forceUpdate: Boolean, tasks: List<Task>) {
        // don't allow the data sources to complete.
        `when`(dataSource.tasks(forceUpdate)).thenReturn(Observable.just(tasks).concatWith(Observable.never()))
    }

    private fun setTasksNotAvailable(dataSource: TaskDataSource, forceUpdate: Boolean) {
        `when`(dataSource.tasks(forceUpdate)).thenReturn(Observable.just(emptyList()))
    }

    companion object {

        private val task1 = Task(1, "Title1", "Description1", false)
        private val task2 = Task(2, "Title2", "Description2", true)
        private val TASKS = object : ArrayList<Task>() {
            init {
                add(task1)
                add(task2)
            }
        }
    }
}
