package djordjeh.architecture.mvp.data

import com.squareup.sqlbrite3.BriteDatabase

import javax.inject.Named
import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import djordjeh.architecture.mvp.data.source.TaskDataSource
import djordjeh.architecture.mvp.data.source.local.DatabaseModule
import djordjeh.architecture.mvp.data.source.local.TaskLocalDataSource
import djordjeh.architecture.mvp.data.source.remote.TaskRemoteDataSource

@Module(includes = [DatabaseModule::class])
class DataModule {

    @Provides
    @Singleton
    internal fun taskRepository(@Named(LOCAL) local: TaskDataSource, @Named(REMOTE) remote: TaskDataSource): TaskDataSource {
        return TaskRepository(local, remote)
    }

    @Provides
    @Singleton
    @Named(LOCAL)
    internal fun taskLocalDataSource(briteDatabase: BriteDatabase): TaskDataSource {
        return TaskLocalDataSource(briteDatabase)
    }

    @Provides
    @Singleton
    @Named(REMOTE)
    internal fun taskRemoteDataSource(): TaskDataSource {
        return TaskRemoteDataSource()
    }

    companion object {
        private const val REMOTE = "remote"
        private const val LOCAL = "local"
    }
}
