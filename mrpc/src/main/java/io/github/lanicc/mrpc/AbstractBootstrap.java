package io.github.lanicc.mrpc;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 2022/7/11.
 *
 * @author lan
 */
public abstract class AbstractBootstrap {

    protected final Config config;

    private final AtomicInteger lifeCycle = new AtomicInteger(-1);

    private final int NEW = -1;
    private final int INIT = 0;
    private final int START = 1;
    private final int STOP = 2;


    protected AbstractBootstrap(Config config) {
        this.config = Objects.requireNonNull(config);
    }

    public void init() {
        if (!lifeCycle.compareAndSet(NEW, INIT)) {
            return;
        }
        doInit();
    }

    protected void doInit() {

    }

    public void start() {
        if (!lifeCycle.compareAndSet(INIT, START)) {
            throw new IllegalStateException("started already");
        }
        doStart();
    }

    protected void doStart() {

    }

    public void stop() {
        if (!lifeCycle.compareAndSet(START, STOP)) {
            throw new IllegalStateException("not start");
        }
        doStop();
    }

    protected void doStop() {

    }

    protected void checkRunning() {
        if (lifeCycle.get() != START) {
            throw new IllegalStateException("not start");
        }
    }



}
