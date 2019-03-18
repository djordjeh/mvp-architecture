package djordjeh.architecture.mvp.ui;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import djordjeh.architecture.mvp.dagger.scope.FragmentScope;
import djordjeh.architecture.mvp.ui.tasks.TasksFragment;
import djordjeh.architecture.mvp.ui.tasks.TasksModule;

@Module
public abstract class HomeUIModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = TasksModule.class)
    abstract TasksFragment contributeTasksFragment();
}
