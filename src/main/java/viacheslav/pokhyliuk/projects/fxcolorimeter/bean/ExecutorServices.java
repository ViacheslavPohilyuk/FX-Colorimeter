package viacheslav.pokhyliuk.projects.fxcolorimeter.bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public enum ExecutorServices {
    INSTANCE;

    ExecutorServices() {
        this.cachedExecutor = Executors.newCachedThreadPool();
        this.scheduler = Executors.newScheduledThreadPool(3);
    }

    private ExecutorService cachedExecutor;
    private ScheduledExecutorService scheduler;

    public static ExecutorService getCached() {
        return INSTANCE.cachedExecutor;
    }

    public static ScheduledExecutorService getScheduled() {
        return INSTANCE.scheduler;
    }

    public static void clear() {
        getCached().shutdownNow();
        getScheduled().shutdownNow();
    }
}
