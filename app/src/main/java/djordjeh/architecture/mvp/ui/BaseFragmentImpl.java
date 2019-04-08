package djordjeh.architecture.mvp.ui;

import androidx.annotation.NonNull;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class BaseFragmentImpl<P extends BasePresenter> extends DaggerFragment {

    @NonNull
    @Inject
    public P presenter;

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
