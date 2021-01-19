package net.xilla.core.library.program;

import lombok.Getter;
import net.xilla.core.library.manager.ManagerObject;

public abstract class StartupProcess extends ManagerObject implements Runnable {

    @Getter
    private StartupPriority priority;

    public StartupProcess(String name, StartupPriority priority) {
        super(name, "");

        this.priority = priority;
    }

}
