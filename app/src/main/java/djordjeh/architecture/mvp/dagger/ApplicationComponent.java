package djordjeh.architecture.mvp.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import djordjeh.architecture.mvp.data.DataModule;

@Singleton
@Component(modules = {
        ActivityBindingModule.class,
        ApplicationModule.class,
        DataModule.class
})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }

    @Override
    void inject(DaggerApplication application);
}
