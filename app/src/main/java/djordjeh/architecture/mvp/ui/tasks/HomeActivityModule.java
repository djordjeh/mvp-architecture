package djordjeh.architecture.mvp.ui.tasks;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import djordjeh.architecture.mvp.dagger.scope.FragmentScope;

@Module
public abstract class HomeActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = TasksModule.class)
    abstract TasksFragment contributeTasksFragment();
}
