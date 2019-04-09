package djordjeh.architecture.mvp.ui.tasks

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import djordjeh.architecture.mvp.dagger.scope.FragmentChildScope
import djordjeh.architecture.mvp.dagger.scope.FragmentScope
import djordjeh.architecture.mvp.ui.task.TaskFragment
import djordjeh.architecture.mvp.ui.task.TaskModule

@Module
abstract class TasksModule {

    @Binds
    @FragmentScope
    internal abstract fun bindView(fragment: TasksFragment): TasksContract.View

    @Binds
    @FragmentScope
    internal abstract fun bindPresenter(presenter: TasksPresenter): TasksContract.Presenter

    @FragmentChildScope
    @ContributesAndroidInjector(modules = [TaskModule::class])
    internal abstract fun contributeTaskFragment(): TaskFragment
}
