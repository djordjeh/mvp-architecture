package djordjeh.architecture.mvp

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import djordjeh.architecture.mvp.dagger.DaggerApplicationComponent

class ToDoApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }
}
