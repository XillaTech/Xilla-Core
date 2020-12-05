package net.xilla.core.library.worker;

import lombok.Getter;
import net.xilla.core.library.manager.Manager;

public class WorkerManager extends Manager<Worker> {

    @Getter
    private static WorkerManager instance = new WorkerManager();

    public WorkerManager() {
        super("Workers");
    }

    @Override
    protected void load() {

    }

    @Override
    protected void objectAdded(Worker obj) {

    }

    @Override
    protected void objectRemoved(Worker obj) {

    }
}
