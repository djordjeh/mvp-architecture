package djordjeh.architecture.mvp.util

import android.text.TextUtils
import android.view.View
import android.view.WindowManager

import com.google.android.material.textfield.TextInputLayout

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Root
import androidx.test.espresso.matcher.BoundedMatcher

object CustomMatchers {

    // windowToken == appToken means this window isn't contained by any other windows.
    // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
    fun isToast(): Matcher<Root>{
        return object : TypeSafeMatcher<Root>() {
            override fun matchesSafely(root: Root): Boolean {
                val type = root.windowLayoutParams.get().type
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    val windowToken = root.decorView.windowToken
                    val appToken = root.decorView.applicationWindowToken
                    return windowToken === appToken
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("is Toast message")
            }
        }
    }

    fun withItemCount(count: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

            override fun describeTo(description: Description) {
                description.appendText(String.format("RecyclerView with item count: %d", count))
            }

            override fun matchesSafely(item: RecyclerView): Boolean {
                return item.adapter?.itemCount == count
            }
        }
    }

    fun shouldRefreshing(refreshing: Boolean): Matcher<View> {
        return object : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

            override fun describeTo(description: Description) {
                description.appendText(if (refreshing) "is refreshing" else "not refreshing")
            }

            override fun matchesSafely(item: SwipeRefreshLayout): Boolean {
                return refreshing == item.isRefreshing
            }
        }
    }

    fun isErrorDisplayed(errorMessage: CharSequence?): Matcher<View> {
        return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {

            override fun describeTo(description: Description) {
                description.appendText(String.format("Is error displayed %s", errorMessage))
            }

            override fun matchesSafely(item: TextInputLayout): Boolean {
                return if (errorMessage == null) {
                    !item.isErrorEnabled && TextUtils.isEmpty(item.error)
                } else {
                    item.isErrorEnabled && errorMessage == item.error
                }
            }
        }
    }
}
