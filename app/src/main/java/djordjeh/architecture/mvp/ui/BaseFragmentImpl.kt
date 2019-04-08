package djordjeh.architecture.mvp.ui

import javax.inject.Inject

import dagger.android.support.DaggerFragment

open class BaseFragmentImpl<P : BasePresenter> : DaggerFragment() {

    @Inject
    lateinit var presenter: P

    override fun onDestroyView() {
        this.presenter.stop()
        super.onDestroyView()
    }

    override fun onDestroy() {
        this.presenter.destroy()
        super.onDestroy()
    }
}
