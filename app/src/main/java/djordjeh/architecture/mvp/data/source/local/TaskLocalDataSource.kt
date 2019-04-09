package djordjeh.architecture.mvp.data.source.local

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import com.squareup.sqlbrite3.BriteDatabase

import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.data.model.Task
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class TaskLocalDataSource(private val database: BriteDatabase) : TaskDataSource {

    override fun tasks(forceUpdate: Boolean): Observable<List<Task>> {
        val query = String.format("SELECT * FROM %s ORDER BY %s DESC", AppSQLiteHelper.TaskEntity.TABLE_NAME, AppSQLiteHelper.TaskEntity.ID)
        return database.createQuery(AppSQLiteHelper.TaskEntity.TABLE_NAME, query).mapToList { this.getTask(it) }
    }

    override fun task(taskId: Long): Maybe<Task> {
        val query = String.format("SELECT * FROM %s WHERE %s = ?", AppSQLiteHelper.TaskEntity.TABLE_NAME, AppSQLiteHelper.TaskEntity.ID)
        return database.createQuery(AppSQLiteHelper.TaskEntity.TABLE_NAME, query, taskId.toString()).mapToOne<Task> { this.getTask(it) }.firstElement()
    }

    override fun save(task: Task): Single<Task> {
        val rowId = database.insert(AppSQLiteHelper.TaskEntity.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, taskContent(task))
        return if (rowId > 0) Single.just(task) else Single.error(IllegalArgumentException("Task can't be saved"))
    }

    override fun delete(task: Task): Single<Task> {
        val whereClause = AppSQLiteHelper.TaskEntity.ID + " = ?"
        val rowId = database.delete(AppSQLiteHelper.TaskEntity.TABLE_NAME, whereClause, task.id.toString()).toLong()
        return if (rowId > 0) Single.just(task) else Single.error(IllegalArgumentException("Task not find"))
    }

    private fun taskContent(task: Task): ContentValues {
        return ContentValues().apply {
            put(AppSQLiteHelper.TaskEntity.ID, if (task.id == 0L) System.currentTimeMillis() else task.id)
            put(AppSQLiteHelper.TaskEntity.TITLE, task.title)
            put(AppSQLiteHelper.TaskEntity.DESCRIPTION, task.description)
            put(AppSQLiteHelper.TaskEntity.COMPLETED, if (task.completed) 1 else 0)
        }
    }

    private fun getTask(cursor: Cursor): Task {
        return Task(
                cursor.getLong(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity.ID)),
                cursor.getString(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity.TITLE)),
                cursor.getString(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity.DESCRIPTION)),
                cursor.getInt(cursor.getColumnIndex(AppSQLiteHelper.TaskEntity.COMPLETED)) == 1
        )
    }
}
