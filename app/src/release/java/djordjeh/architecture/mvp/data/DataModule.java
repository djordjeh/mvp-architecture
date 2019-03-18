package djordjeh.architecture.mvp.data;

import com.squareup.sqlbrite3.BriteDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import djordjeh.architecture.mvp.data.source.TaskDataSource;
import djordjeh.architecture.mvp.data.source.local.DatabaseModule;
import djordjeh.architecture.mvp.data.source.local.TaskLocalDataSource;
import djordjeh.architecture.mvp.data.source.remote.TaskRemoteDataSource;

@Module(includes = DatabaseModule.class)
public class DataModule {

    private static final String REMOTE  = "remote";
    private static final String LOCAL   = "local";

    @Provides
    @Singleton
    TaskDataSource taskRepository(@Named(LOCAL) TaskDataSource local, @Named(REMOTE) TaskDataSource remote) {
        return new TaskRepository(local, remote);
    }

    @Provides
    @Singleton
    @Named(LOCAL)
    TaskDataSource taskLocalDataSource(BriteDatabase briteDatabase) {
        return new TaskLocalDataSource(briteDatabase);
    }

    @Provides
    @Singleton
    @Named(REMOTE)
    TaskDataSource taskRemoteDataSource() {
        return new TaskRemoteDataSource();
    }
}
