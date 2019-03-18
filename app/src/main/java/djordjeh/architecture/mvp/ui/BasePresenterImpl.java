package djordjeh.architecture.mvp.ui;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

abstract public class BasePresenterImpl<V> implements BasePresenter {

    @NonNull
    protected final V view;

    @NonNull
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BasePresenterImpl(@NonNull V view) {
        this.view = view;
    }

    @Override
    public void stop() {
        compositeDisposable.clear();
    }

    @Override
    public void destroy() {
        if (!this.compositeDisposable.isDisposed()) {
            this.compositeDisposable.dispose();
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (!this.compositeDisposable.isDisposed()) {
            this.compositeDisposable.add(disposable);
        }
    }

    protected boolean removeDisposable(Disposable disposable) {
        return this.compositeDisposable.remove(disposable);
    }
}
