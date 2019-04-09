package djordjeh.architecture.mvp.dagger


import javax.inject.Singleton

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

@Singleton
@Component(modules = [ActivityBindingModule::class, TestApplicationModule::class])
interface TestApplicationComponent : AndroidInjector<DaggerApplication> {

    override fun inject(application: DaggerApplication)
}
