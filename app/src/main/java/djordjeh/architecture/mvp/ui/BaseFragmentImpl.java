package djordjeh.architecture.mvp.ui;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class BaseFragmentImpl<P extends BasePresenter> extends DaggerFragment {

    @NonNull
    @Inject
    protected P presenter;

    @Override
    public void onDestroyView() {
        this.presenter.stop();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        this.presenter.destroy();
        super.onDestroy();
    }
}
