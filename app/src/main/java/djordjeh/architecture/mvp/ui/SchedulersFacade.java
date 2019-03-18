package djordjeh.architecture.mvp.ui;

import io.reactivex.Scheduler;

public interface SchedulersFacade {
    Scheduler io();
    Scheduler computation();
    Scheduler mainThread();
}
