package djordjeh.architecture.mvp.data.source.local

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

class AppSQLiteHelper
/**
 * Creates a new Callback to get database lifecycle events.
 *
 * @param dbVersion The version for the database instance. See [.version].
 */
internal constructor(dbVersion: Int) : SupportSQLiteOpenHelper.Callback(dbVersion) {

    override fun onCreate(db: SupportSQLiteDatabase) {
        db.execSQL(CREATE_TABLE_TASKS)
    }

    override fun onUpgrade(db: SupportSQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntity.TABLE_NAME + ";")
        onCreate(db)
    }

    internal class TaskEntity {
        companion object {
            const val ID = "_id"
            const val TABLE_NAME = "tasks"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
            const val COMPLETED = "completed"
        }
    }

    companion object {

        private const val INTEGER_TYPE = " INTEGER"
        private const val TEXT_TYPE = " TEXT"
        private const val COMMA_SEP = ", "

        private const val CREATE_TABLE_TASKS = "CREATE TABLE " + TaskEntity.TABLE_NAME + " (" +
                TaskEntity.ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                TaskEntity.TITLE + TEXT_TYPE + COMMA_SEP +
                TaskEntity.DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                TaskEntity.COMPLETED + INTEGER_TYPE + " );"
    }
}
