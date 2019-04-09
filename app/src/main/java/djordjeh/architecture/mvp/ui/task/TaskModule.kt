package djordjeh.architecture.mvp.ui.task

import dagger.Binds
import dagger.Module
import djordjeh.architecture.mvp.dagger.scope.FragmentChildScope

@Module
abstract class TaskModule {

    @Binds
    @FragmentChildScope
    internal abstract fun bindView(fragment: TaskFragment): ContractTask.View

    @Binds
    @FragmentChildScope
    internal abstract fun bindPresenter(presenter: TaskPresenter): ContractTask.Presenter
}
