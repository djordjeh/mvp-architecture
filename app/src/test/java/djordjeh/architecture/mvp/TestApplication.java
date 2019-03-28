package djordjeh.architecture.mvp;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import djordjeh.architecture.mvp.dagger.DaggerTestApplicationComponent;

public class TestApplication extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerTestApplicationComponent.create();
    }
}
