package djordjeh.architecture.mvp.ui.task

import android.content.Intent

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.shadows.ShadowToast

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import djordjeh.architecture.mvp.ActivityTest
import djordjeh.architecture.mvp.R
import djordjeh.architecture.mvp.data.model.Task

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import dagger.android.AndroidInjector
import djordjeh.architecture.mvp.util.CustomMatchers.isErrorDisplayed
import org.junit.Assert.assertTrue
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class TaskFragmentTest {

    private val fragment = TaskFragment.newInstance(TASK)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Mock
    internal lateinit var presenter: ContractTask.Presenter

    @get:Rule
    var activityRule: ActivityTestRule<ActivityTest> = object : ActivityTestRule<ActivityTest>(ActivityTest::class.java, true, false) {

        override fun afterActivityLaunched() {
            activity.startFragment(fragment, AndroidInjector {
                if (it is TaskFragment) {
                    fragment.presenter = this@TaskFragmentTest.presenter
                }
            })
        }
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val intent = Intent(context, ActivityTest::class.java)
        activityRule.launchActivity(intent)
    }

    @Test
    fun checkViews_AndGetTaskFromPresenter() {
        onView(withId(R.id.editTextTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.editTextDescription)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonSubmit)).check(matches(isDisplayed()))
        verify<ContractTask.Presenter>(presenter).getTask(TASK.id)
    }

    @Test
    fun showTaskTest_andAfterScreenRotation() {
        fragment.showTask(TASK)
        onView(withId(R.id.editTextTitle)).check(matches(withText(TASK.title)))
        onView(withId(R.id.editTextDescription)).check(matches(withText(TASK.description)))
    }

    @Test
    fun showEmptyTitleErrorTest_andTypeNewTitle() {
        fragment.showEmptyTitleError(true)
        onView(withId(R.id.inputTitle)).check(matches(isErrorDisplayed(context.getString(R.string.error_empty_title))))

        onView(withId(R.id.editTextTitle)).perform(clearText(), typeText(TASK.title!!), closeSoftKeyboard())
        onView(withId(R.id.inputTitle)).check(matches(isErrorDisplayed(null)))
    }

    @Test
    fun onTaskSavedShowMessageTest() {
        fragment.onTaskSaved(TASK)
        assertTrue(ShadowToast.showedToast(context.getString(R.string.task_saved, TASK.title)))
    }

    @Test
    fun showError() {
        fragment.showError(ERROR)
        assertTrue(ShadowToast.showedToast(ERROR.message))
    }

    companion object {

        private val ERROR = Throwable("No Internet connection")
        private val TASK = Task(1, "Task title", "Task description", false)
    }
}
