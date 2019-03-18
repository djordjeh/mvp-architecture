package djordjeh.architecture.mvp;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import djordjeh.architecture.mvp.dagger.DaggerApplicationComponent;

public class ToDoApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerApplicationComponent.builder().application(this).build();
    }
}
