package net.xilla.core.library.worker;

import lombok.Getter;
import net.xilla.core.library.manager.Manager;

public class WorkerManager extends Manager<String, Worker> {

    @Getter
    private static WorkerManager instance = new WorkerManager();

    public WorkerManager() {
        super("Workers");
    }

}
