package djordjeh.architecture.mvp.dagger;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import djordjeh.architecture.mvp.TestApplication;

@Singleton
@Component(modules = {
        ActivityBindingModule.class,
        TestApplicationModule.class
})
interface TestApplicationComponent extends AndroidInjector<TestApplication> {

    @Override
    void inject(TestApplication application);
}
