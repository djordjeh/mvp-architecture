package djordjeh.architecture.mvp.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import djordjeh.architecture.mvp.ToDoApplication;
import djordjeh.architecture.mvp.data.DataModule;

@Singleton
@Component(modules = {
        ActivityBindingModule.class,
        ApplicationModule.class,
        DataModule.class
})
interface ApplicationComponent extends AndroidInjector<ToDoApplication> {

    @Component.Builder
    interface Builder
    {
        @BindsInstance
        Builder application(Application application);

        ApplicationComponent build();
    }

    @Override
    void inject(ToDoApplication application);
}
