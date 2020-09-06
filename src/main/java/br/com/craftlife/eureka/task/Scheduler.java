package br.com.craftlife.eureka.task;

import br.com.craftlife.eureka.module.EurekaModule;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.concurrent.*;

@Singleton
public class Scheduler {

    @Inject
    private EurekaModule module;

    private final ConcurrentHashMap<Integer, ScheduledFuture> futures = new ConcurrentHashMap<>();

    private int currentId = 1;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    public int scheduleAsync(Runnable runner, long delay, TimeUnit unit) {
        int id = currentId++;

        ScheduledFuture future = executor.schedule(() -> {
            runner.run();
            futures.remove(id);
        }, delay, unit);

        futures.put(id, future);
        return id;
    }

    public void runAsync(Runnable runner) {
        scheduleAsync(runner, 1, TimeUnit.MILLISECONDS);
    }

    public void runSync(Runnable runner) {
        module.getServer().getScheduler().runTask(module, runner);
    }

    public int schedule(Runnable runner, long delay, TimeUnit unit) {
        return scheduleAsync(() -> runSync(runner), delay, unit);
    }

    public int scheduleRepeatingAsync(Runnable runner, long delay, long repeat, TimeUnit unit) {
        int id = currentId++;

        ScheduledFuture future = executor.scheduleWithFixedDelay(runner, delay, repeat, unit);

        futures.put(id, future);
        return id;
    }

    public int scheduleRepeating(Runnable runner, long delay, long repeat, TimeUnit unit) {
        return scheduleRepeatingAsync(() -> runSync(runner), delay, repeat, unit);
    }

    public void cancel(int id) {
        ScheduledFuture future = futures.remove(id);
        if (future != null) {
            future.cancel(true);
        }
    }

    public boolean containsTask(int id) {
        return futures.containsKey(id);
    }

    public boolean isCompleted(int id) {
        ScheduledFuture future = futures.get(id);
        if (future != null) {
            return future.isDone();
        }
        return false;
    }

    public void shutdown() throws InterruptedException {
        futures.clear();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

}
