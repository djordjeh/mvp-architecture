package djordjeh.architecture.mvp.ui.task;

import dagger.Binds;
import dagger.Module;
import djordjeh.architecture.mvp.dagger.scope.FragmentChildScope;

@Module
public abstract class TaskModule {

    @Binds
    @FragmentChildScope
    abstract ContractTask.View bindView(TaskFragment fragment);

    @Binds
    @FragmentChildScope
    abstract ContractTask.Presenter bindPresenter(TaskPresenter presenter);
}
