package djordjeh.architecture.mvp.data.source.local;

import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import android.content.Context;

import com.squareup.sqlbrite3.BriteDatabase;
import com.squareup.sqlbrite3.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;

@Module
public class DatabaseModule {

    private static final String DB_NAME = "todo.db";
    private static final int DB_VERSION = 1;

    @Provides
    @Singleton
    BriteDatabase database(SupportSQLiteOpenHelper helper) {
        return new SqlBrite.Builder().build().wrapDatabaseHelper(helper, Schedulers.io());
    }

    @Provides
    @Singleton
    SupportSQLiteOpenHelper sqLiteOpenHelper(SupportSQLiteOpenHelper.Configuration configuration) {
        return new FrameworkSQLiteOpenHelperFactory().create(configuration);
    }

    @Provides
    @Singleton
    SupportSQLiteOpenHelper.Configuration sqlConfiguration(Context context, SupportSQLiteOpenHelper.Callback callback) {
        return SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(DB_NAME)
                .callback(callback)
                .build();
    }

    @Provides
    @Singleton
    SupportSQLiteOpenHelper.Callback mySQLiteCallback() {
        return new AppSQLiteHelper(DB_VERSION);
    }
}
