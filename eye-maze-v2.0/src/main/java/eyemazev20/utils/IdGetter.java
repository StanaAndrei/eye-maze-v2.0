package eyemazev20.utils;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class IdGetter {
    private AtomicLong curr;
    private AtomicReference<Queue<Long>> priorityQueue;

    public IdGetter(final long start) {
        priorityQueue = new AtomicReference<>(new PriorityQueue<>());
        curr = new AtomicLong(start);
    }

    public void remove(final long id) {
        if (id <= curr.get()) {
            priorityQueue.get().add(id);
        }
    }

    public long get() {
        if (!priorityQueue.get().isEmpty()) {
            return priorityQueue.get().poll();
        }
        return curr.getAndIncrement();
    }
}