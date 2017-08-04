package org.jetlang.core;

/**
 * Default implementation that simply executes all events.
 *
 * @author mrettig
 */
public class BatchExecutorImpl implements BatchExecutor {
    public void execute(Iterable<Runnable> toExecute) {
        for (Runnable runnable : toExecute) {
            runnable.run();
        }
    }
}
