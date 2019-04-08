package djordjeh.architecture.mvp.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import djordjeh.architecture.mvp.dagger.scope.ActivityScope
import djordjeh.architecture.mvp.ui.tasks.HomeActivityModule
import djordjeh.architecture.mvp.ui.tasks.HomeActivity

@Module(includes = [AndroidSupportInjectionModule::class])
internal abstract class ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeActivityModule::class])
    internal abstract fun contributeHomeActivity(): HomeActivity
}
