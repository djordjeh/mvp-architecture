package djordjeh.architecture.mvp.ui

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenterImpl<V>(protected val view: V) : BasePresenter {

    private val compositeDisposable = CompositeDisposable()

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun destroy() {
        if (!this.compositeDisposable.isDisposed) {
            this.compositeDisposable.dispose()
        }
    }

    protected fun addDisposable(disposable: Disposable) {
        if (!this.compositeDisposable.isDisposed) {
            this.compositeDisposable.add(disposable)
        }
    }

    protected fun removeDisposable(disposable: Disposable): Boolean {
        return this.compositeDisposable.remove(disposable)
    }
}
