package djordjeh.architecture.mvp.ui

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulersFacadeImpl : SchedulersFacade {

    /**
     * IO thread pool scheduler
     */
    override fun io() = Schedulers.io()

    /**
     * Computation thread pool scheduler
     */
    override fun computation() = Schedulers.computation()

    /**
     * Main Thread scheduler
     */
    override fun mainThread() = AndroidSchedulers.mainThread()
}
