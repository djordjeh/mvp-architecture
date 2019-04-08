package djordjeh.architecture.mvp.ui

import io.reactivex.Scheduler

interface SchedulersFacade {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun mainThread(): Scheduler
}
