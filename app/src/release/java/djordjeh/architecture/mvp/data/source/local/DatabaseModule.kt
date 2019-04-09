package djordjeh.architecture.mvp.data.source.local

import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import android.content.Context

import com.squareup.sqlbrite3.BriteDatabase
import com.squareup.sqlbrite3.SqlBrite

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers

@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun database(helper: SupportSQLiteOpenHelper): BriteDatabase {
        return SqlBrite.Builder().build().wrapDatabaseHelper(helper, Schedulers.io())
    }

    @Provides
    @Singleton
    internal fun sqLiteOpenHelper(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        return FrameworkSQLiteOpenHelperFactory().create(configuration)
    }

    @Provides
    @Singleton
    internal fun sqlConfiguration(context: Context, callback: SupportSQLiteOpenHelper.Callback): SupportSQLiteOpenHelper.Configuration {
        return SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(DB_NAME)
                .callback(callback)
                .build()
    }

    @Provides
    @Singleton
    internal fun mySQLiteCallback(): SupportSQLiteOpenHelper.Callback {
        return AppSQLiteHelper(DB_VERSION)
    }

    companion object {
        private const val DB_NAME = "todo.db"
        private const val DB_VERSION = 1
    }
}
