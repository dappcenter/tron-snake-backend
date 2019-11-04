package de.lj.tronsnakebackend.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Component
public class BroadcastScheduler extends ThreadPoolTaskScheduler {

    private final Map<Object, ScheduledFuture<?>> scheduledBroadcasts = new IdentityHashMap<>();

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable broadcast, long period) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(broadcast, period);

        scheduledBroadcasts.put(broadcast, future);

        return future;
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable broadcast, long period) {
        ScheduledFuture<?> future = super.scheduleWithFixedDelay(broadcast, period);
        scheduledBroadcasts.put(broadcast, future);

        return future;
    }

    public void cancelBroadcast(Runnable broadcast) {
        scheduledBroadcasts.get(broadcast).cancel(true);
        scheduledBroadcasts.remove(broadcast);
    }
}