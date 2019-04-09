package djordjeh.architecture.mvp.ui.tasks

import dagger.Module
import dagger.android.ContributesAndroidInjector
import djordjeh.architecture.mvp.dagger.scope.FragmentScope

@Module
abstract class HomeActivityModule {

    @FragmentScope
    @ContributesAndroidInjector(modules = [TasksModule::class])
    internal abstract fun contributeTasksFragment(): TasksFragment
}
