package djordjeh.architecture.mvp.ui.tasks;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import djordjeh.architecture.mvp.BuildConfig;
import djordjeh.architecture.mvp.data.model.Task;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@Config(sdk = Build.VERSION_CODES.P)
@RunWith(RobolectricTestRunner.class)
public class TaskAdapterTest {

    private Context context;

    @Before
    public void setup() {
        context = RuntimeEnvironment.systemContext;
    }

    @Test
    public void postsAdapterViewRecyclingCaption() {
        final List<Task> tasks = Arrays.asList(
                new Task(1, "Title", "Description", false),
                new Task(2, "Title 2", "Description 2", true)
        );

        TasksAdapter adapter = new TasksAdapter();
        adapter.setList(tasks);

        RecyclerView recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        // Run test
        TasksAdapter.TaskViewHolder viewHolder = adapter.onCreateViewHolder(recyclerView, 0);

        adapter.onBindViewHolder(viewHolder, 0);

        assertFalse(viewHolder.binding.checkBox.isChecked());


        assertEquals(adapter.getItemCount(), 2);
    }
}
