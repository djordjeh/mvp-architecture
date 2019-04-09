package djordjeh.architecture.mvp.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.recyclerview.widget.RecyclerView

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import djordjeh.architecture.mvp.R
import djordjeh.architecture.mvp.ToDoApplication
import djordjeh.architecture.mvp.dagger.TestApplicationModule
import djordjeh.architecture.mvp.data.model.Task
import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.ui.tasks.HomeActivity
import io.reactivex.Maybe
import io.reactivex.Observable

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import djordjeh.architecture.mvp.dagger.DaggerTestApplicationComponent
import djordjeh.architecture.mvp.util.CustomMatchers.isToast
import djordjeh.architecture.mvp.util.CustomMatchers.withItemCount
import org.hamcrest.CoreMatchers.not

@RunWith(AndroidJUnit4::class)
@LargeTest
class HomeActivityTest {

    companion object {

        private const val EMPTY = ""
        private val OLD_TASK = Task(1, "Old task", "Description of the old task", true)
        private val NEW_TASK = Task(2, "New task", "Description of the new task", false)
        private val TASK_LIST = listOf(OLD_TASK, NEW_TASK)
    }

    @Mock
    private lateinit var taskDataSource: TaskDataSource

    @get:Rule
    var activityRule = ActivityTestRule(HomeActivity::class.java, true, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val application = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as ToDoApplication

        val component = DaggerTestApplicationComponent.builder()
                .testApplicationModule(TestApplicationModule(taskDataSource))
                .build()
        component.inject(application)

        Mockito.doReturn(Observable.just(TASK_LIST)).`when`(taskDataSource).tasks(false)
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, HomeActivity::class.java))
    }

    @Test
    fun checkEmptyList() {
        Mockito.doReturn(Observable.just(arrayListOf<Task>())).`when`(taskDataSource).tasks(true)
        // Swipe down for refresh and return empty array
        onView(withId(R.id.recyclerTasks)).perform(swipeDown())

        // Check if recycler view is empty
        onView(withId(R.id.recyclerTasks)).check(matches(withItemCount(0)))

        // Check if empty text view is displayed
        onView(withId(R.id.textEmptyList)).check(matches(isDisplayed()))
    }

    @Test
    fun checkNonEmptyList() {
        // By default we should have two tasks from @TASK_LIST
        onView(withId(R.id.recyclerTasks)).check(matches(withItemCount(2)))

        // Empty text view should not be displayed
        onView(withId(R.id.textEmptyList)).check(matches(not(isDisplayed())))

        // Check if OLD_TASK & NEW_TASK are displayed
        onView(withText(OLD_TASK.title)).check(matches(isDisplayed()))
        onView(withText(NEW_TASK.title)).check(matches(isDisplayed()))
    }

    @Test
    fun showTaskDetails_andEditOldTask() {
        Mockito.doReturn(Maybe.just(OLD_TASK)).`when`<TaskDataSource>(taskDataSource).task(OLD_TASK.id)

        // Click on first task in list @OLD_TASK
        onView(withId(R.id.recyclerTasks)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // From some reason I'm not able to make it to work, always returns index -1
        //        onView(withId(R.id.recyclerTasks)).perform(RecyclerViewActions.actionOnItem(
        //                hasDescendant(withText(OLD_TASK.getTitle())), click()));

        // Check if Task title and description are displayed
        onView(withId(R.id.editTextTitle)).check(matches(withText(OLD_TASK.title)))
        onView(withId(R.id.editTextDescription)).check(matches(withText(OLD_TASK.description)))

        // Edit Task title
        onView(withId(R.id.editTextTitle)).perform(clearText(), typeText("Old task edited"), closeSoftKeyboard())

        // Rotate The screen
        activityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Save the task
        onView(withId(R.id.buttonSubmit)).perform(click())

        // Verify save task is displayed on screen.
        onView(withText(context.getString(R.string.task_saved, "Old task edited"))).inRoot(isToast()).check(matches(isDisplayed()))
    }

    @Test
    fun showNewTaskScreen_andAddNewTask() {
        // Click on the add task button
        onView(withId(R.id.buttonAdd)).perform(click())

        // Check if Task title and description are empty
        onView(withId(R.id.editTextTitle)).check(matches(withText(EMPTY)))
        onView(withId(R.id.editTextDescription)).check(matches(withText(EMPTY)))

        // Enter task title and description
        onView(withId(R.id.editTextTitle)).perform(typeText(NEW_TASK.title), closeSoftKeyboard())
        onView(withId(R.id.editTextDescription)).perform(typeText(NEW_TASK.description!!), closeSoftKeyboard())

        // Save the task
        onView(withId(R.id.buttonSubmit)).perform(click())

        // Verify save task is displayed on screen.
        onView(withText(context.getString(R.string.task_saved, NEW_TASK.title))).inRoot(isToast()).check(matches(isDisplayed()))
    }

    @Test
    fun deleteTask_undoDelete() {
        // Swipe task from list to delete it
        onView(withId(R.id.recyclerTasks)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, swipeLeft()))

        // Verify Snackbar is displayed with proper message
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.task_deleted)))

        // Verify task is not displayed on screen in the task list.
        onView(withText(OLD_TASK.title)).check(doesNotExist())

        // Click on UNDO action in Snackbar
        onView(withId(com.google.android.material.R.id.snackbar_action)).perform(click())

        // Verify Toast message for save task is displayed on screen.
        onView(withText(context.getString(R.string.task_completed, OLD_TASK.title))).inRoot(isToast()).check(matches(isDisplayed()))
    }


}
