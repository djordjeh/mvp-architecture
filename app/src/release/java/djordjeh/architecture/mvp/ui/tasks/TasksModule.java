package djordjeh.architecture.mvp.ui.tasks;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import djordjeh.architecture.mvp.dagger.scope.FragmentChildScope;
import djordjeh.architecture.mvp.dagger.scope.FragmentScope;
import djordjeh.architecture.mvp.ui.task.TaskFragment;
import djordjeh.architecture.mvp.ui.task.TaskModule;

@Module
public abstract class TasksModule {

    @Binds
    @FragmentScope
    abstract TasksContract.View bindView(TasksFragment fragment);

    @Binds
    @FragmentScope
    abstract TasksContract.Presenter bindPresenter(TasksPresenter presenter);

    @FragmentChildScope
    @ContributesAndroidInjector(modules = TaskModule.class)
    abstract TaskFragment contributeTaskFragment();
}
