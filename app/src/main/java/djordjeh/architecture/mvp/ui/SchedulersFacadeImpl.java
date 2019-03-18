package djordjeh.architecture.mvp.ui;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulersFacadeImpl implements SchedulersFacade {

    /**
     * IO thread pool scheduler
     */
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    /**
     * Computation thread pool scheduler
     */
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    /**
     * Main Thread scheduler
     */
    @Override
    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }
}
