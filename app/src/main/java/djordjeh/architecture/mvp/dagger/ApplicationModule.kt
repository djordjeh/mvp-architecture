package djordjeh.architecture.mvp.dagger

import android.app.Application
import android.content.Context

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import djordjeh.architecture.mvp.ui.SchedulersFacade
import djordjeh.architecture.mvp.ui.SchedulersFacadeImpl

@Module
internal class ApplicationModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideSchedulersFacade(): SchedulersFacade {
        return SchedulersFacadeImpl()
    }
}
