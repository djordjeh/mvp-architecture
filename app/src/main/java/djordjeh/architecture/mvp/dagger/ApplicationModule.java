package djordjeh.architecture.mvp.dagger;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import djordjeh.architecture.mvp.ui.SchedulersFacade;
import djordjeh.architecture.mvp.ui.SchedulersFacadeImpl;

@Module
class ApplicationModule {

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    SchedulersFacade provideSchedulersFacade() {
        return new SchedulersFacadeImpl();
    }
}
