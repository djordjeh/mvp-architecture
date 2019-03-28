package djordjeh.architecture.mvp.dagger;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import djordjeh.architecture.mvp.dagger.scope.ActivityScope;
import djordjeh.architecture.mvp.ui.tasks.HomeActivityModule;
import djordjeh.architecture.mvp.ui.tasks.HomeActivity;

@Module(includes = AndroidSupportInjectionModule.class)
abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = HomeActivityModule.class)
    abstract HomeActivity contributeHomeActivity();
}
