package djordjeh.architecture.mvp.data.source.local;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.provider.BaseColumns;

public class AppSQLiteHelper extends SupportSQLiteOpenHelper.Callback {

    private static final String INTEGER_TYPE = " INTEGER";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";

    static class TaskEntity implements BaseColumns {
        static final String TABLE_NAME       = "tasks";
        static final String TITLE            = "title";
        static final String DESCRIPTION      = "description";
        static final String COMPLETED        = "completed";
    }

    private static final String CREATE_TABLE_TASKS =
            "CREATE TABLE " + TaskEntity.TABLE_NAME + " (" +
                    TaskEntity._ID + INTEGER_TYPE + " PRIMARY KEY" + COMMA_SEP +
                    TaskEntity.TITLE + TEXT_TYPE + COMMA_SEP +
                    TaskEntity.DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    TaskEntity.COMPLETED + INTEGER_TYPE + " );";

    /**
     * Creates a new Callback to get database lifecycle events.
     *
     * @param dbVersion The version for the database instance. See {@link #version}.
     */
    AppSQLiteHelper(int dbVersion) {
        super(dbVersion);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntity.TABLE_NAME + ";");
        onCreate(db);
    }
}
