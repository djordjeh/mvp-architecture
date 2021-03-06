package djordjeh.architecture.mvp.util;

import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.test.espresso.Root;
import androidx.test.espresso.matcher.BoundedMatcher;

public class CustomMatchers {

    public static Matcher<View> withItemCount(int count) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("RecyclerView with item count: %d", count));
            }

            @Override
            protected boolean matchesSafely(RecyclerView item) {
                return item.getAdapter().getItemCount() == count;
            }
        };
    }

    public static Matcher<View> shouldRefreshing(boolean refreshing) {
        return new BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText(refreshing ? "is refreshing" : "not refreshing");
            }

            @Override
            protected boolean matchesSafely(SwipeRefreshLayout item) {
                return refreshing == item.isRefreshing();
            }
        };
    }

    public static Matcher<View> isErrorDisplayed(@Nullable CharSequence errorMessage) {
        return new BoundedMatcher<View, TextInputLayout>(TextInputLayout.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText(String.format("Is error displayed %s", errorMessage));
            }

            @Override
            protected boolean matchesSafely(TextInputLayout item) {
                if (errorMessage == null) {
                    return !item.isErrorEnabled() && TextUtils.isEmpty(item.getError());
                } else {
                    return item.isErrorEnabled() && errorMessage.equals(item.getError());
                }
            }
        };
    }

    public static Matcher<Root> isToast() {
        return new TypeSafeMatcher<Root>() {
            @Override
            protected boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    // windowToken == appToken means this window isn't contained by any other windows.
                    // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                    return windowToken == appToken;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is Toast message");
            }
        };
    }
}
