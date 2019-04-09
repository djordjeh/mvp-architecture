package djordjeh.architecture.mvp.ui.tasks

import android.content.Intent

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.shadows.ShadowToast

import java.util.Arrays

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import djordjeh.architecture.mvp.ActivityTest
import djordjeh.architecture.mvp.R
import djordjeh.architecture.mvp.data.model.Task

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import dagger.android.AndroidInjector
import djordjeh.architecture.mvp.util.CustomMatchers.shouldRefreshing
import djordjeh.architecture.mvp.util.CustomMatchers.withItemCount
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertTrue
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class TasksFragmentTest {

    private val fragment = TasksFragment.newInstance()

    @Mock
    internal lateinit var presenter: TasksContract.Presenter

    @get:Rule
    var activityRule: ActivityTestRule<ActivityTest> = object : ActivityTestRule<ActivityTest>(ActivityTest::class.java, true, false) {

        override fun afterActivityLaunched() {
            activity.startFragment(fragment, AndroidInjector {
                if (it is TasksFragment) {
                    fragment.presenter = this@TasksFragmentTest.presenter
                }
            })
        }
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, ActivityTest::class.java)
        activityRule.launchActivity(intent)
    }


    @Test
    fun emptyListViewIsShowed() {
        onView(withId(R.id.recyclerTasks)).check(matches(withItemCount(0)))
        onView(withId(R.id.textEmptyList)).check(matches(isDisplayed()))
    }

    @Test
    fun showTasksAndHideEmptyView() {
        verify<TasksContract.Presenter>(presenter).getTasks(false)
        fragment.showTasks(TASK_LIST)
        onView(withId(R.id.recyclerTasks)).check(matches(withItemCount(2)))
        onView(withId(R.id.textEmptyList)).check(matches(not(isDisplayed())))
        onView(withText(TASK.title)).check(matches(isDisplayed()))
        onView(withText(TASK_2.title)).check(matches(isDisplayed()))
    }

    @Test
    fun pullToRefreshShouldRefreshTheList() {
        onView(withId(R.id.recyclerTasks)).perform(swipeDown())
        onView(withId(R.id.refreshLayout)).check(matches(shouldRefreshing(true)))
        //        verify(presenter).onRefresh();
    }

    @Test
    fun shouldTurnOffRefreshing() {
        fragment.showProgress(false)
        onView(withId(R.id.refreshLayout)).check(matches(shouldRefreshing(false)))
    }

    @Test
    fun buttonAddTask() {
        onView(withId(R.id.buttonAdd)).perform(click())
        verify(presenter).addNewTask()
    }

    @Test
    fun showSavedMessageWithCompletedTask() {
        fragment.showSavedMessage(TASK_2)
        assertTrue(ShadowToast.showedToast(fragment.getString(R.string.task_completed, TASK_2.title)))
    }

    @Test
    fun showSavedMessageWithNotCompletedTask() {
        fragment.showSavedMessage(TASK)
        assertTrue(ShadowToast.showedToast(fragment.getString(R.string.task_not_completed, TASK.title)))
    }

    @Test
    fun showError() {
        fragment.showError(ERROR)
        assertTrue(ShadowToast.showedToast(ERROR.localizedMessage))
    }

    @Test
    fun showDeleteMessage() {
        fragment.showTaskDeletedMessage(TASK)
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText(R.string.task_deleted)))
    }

    @Test
    fun undoActionDelete() {
        fragment.showTaskDeletedMessage(TASK)
        onView(withId(com.google.android.material.R.id.snackbar_action)).perform(click())
        verify<TasksContract.Presenter>(presenter).saveTask(TASK)
    }

    @After
    fun finish() {
        activityRule.finishActivity()
    }

    companion object {

        private val ERROR = Throwable("No Internet connection")
        private val TASK = Task(1, "Title", "Description", false)
        private val TASK_2 = Task(2, "Title2", "Description2", true)
        private val TASK_LIST = Arrays.asList(TASK, TASK_2)
    }
}
