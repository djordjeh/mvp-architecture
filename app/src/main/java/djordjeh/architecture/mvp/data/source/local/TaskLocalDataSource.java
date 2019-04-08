package djordjeh.architecture.mvp.data.source.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.NonNull;

import com.squareup.sqlbrite3.BriteDatabase;

import java.util.List;

import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.data.model.Task;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class TaskLocalDataSource implements TaskDataSource {

    private final BriteDatabase database;

    public TaskLocalDataSource(BriteDatabase database) {
        this.database = database;
    }

    @Override
    public Observable<List<Task>> tasks(boolean forceUpdate) {
        final String query = String.format("SELECT * FROM %s ORDER BY %s DESC", AppSQLiteHelper.TaskEntity.TABLE_NAME, AppSQLiteHelper.TaskEntity._ID);
        return database.createQuery(AppSQLiteHelper.TaskEntity.TABLE_NAME, query).mapToList(this::getTask);
    }

    @Override
    public Maybe<Task> task(long taskId) {
        final String query = String.format("SELECT * FROM %s WHERE %s = ?", AppSQLiteHelper.TaskEntity.TABLE_NAME, AppSQLiteHelper.TaskEntity._ID);
        return database.createQuery(AppSQLiteHelper.TaskEntity.TABLE_NAME, query, String.valueOf(taskId)).mapToOne(this::getTask).firstElement();
    }

    @Override
    public Single<Task> save(@NonNull Task task) {
        final long rowId = database.insert(AppSQLiteHelper.TaskEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, taskContent(task));
        if (rowId != -1) return Single.just(task);
        else return Single.error(new IllegalArgumentException("Task can't be saved"));
    }

    @Override
    public Single<Task> delete(@NonNull Task task) {
        final String whereClause = AppSQLiteHelper.TaskEntity._ID + " = ?";
        final long rowId = database.delete(AppSQLiteHelper.TaskEntity.TABLE_NAME, whereClause, String.valueOf(task.getId()));
        return rowId > 0 ? Single.just(task) : Single.error(new IllegalArgumentException("Task not find"));
    }

    private ContentValues taskContent(@NonNull Task task) {
        final ContentValues values = new ContentValues();
        values.put(AppSQLiteHelper.TaskEntity._ID, task.getId() == 0 ? System.currentTimeMillis() : task.getId());
        values.put(AppSQLiteHelper.TaskEntity.TITLE, task.getTitle());
        values.put(AppSQLiteHelper.TaskEntity.DESCRIPTION, task.getDescription());
        values.put(AppSQLiteHelper.TaskEntity.COMPLETED, task.isCompleted() ? 1 : 0);
        return values;
    }

    private Task getTask(@NonNull Cursor cursor) {
        return new Task(
                cursor.getLong(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity._ID)),
                cursor.getString(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity.TITLE)),
                cursor.getString(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity.COMPLETED)) == 1
        );
    }
}
