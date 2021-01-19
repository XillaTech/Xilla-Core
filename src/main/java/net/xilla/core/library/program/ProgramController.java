package net.xilla.core.library.program;

import net.xilla.core.library.config.Settings;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.manager.ObjectInterface;
import net.xilla.core.library.worker.Worker;

import java.util.Set;

public class ProgramController {

    protected final ProgramManager manager;

    public ProgramController(ProgramManager manager) {
        this.manager = manager;
    }

    public <T extends ObjectInterface> T getObject(String managerName, String key) {
        Manager m = getManager(managerName);
        if(m == null) {
            return null;
        }
        return (T)m.get(key);
    }

    public <T extends ObjectInterface> T getObject(Class clazz, String key) {
        Manager m = getManager(clazz);
        if(m == null) {
            return null;
        }
        return (T)m.get(key);
    }

    public <T extends Manager> T getManager(String name) {
        return (T)this.manager.getXillaManagers().get(manager);
    }

    public <T extends Manager> T getManager(Class clazz) {
        return (T)this.manager.getXillaManagersRefl().get(clazz);
    }

    public <T extends Settings> T getSettings(String name) {
        return (T)manager.getXillaSettings().get(name);
    }

    public <T extends Settings> T getSettings(Class clazz) {
        return (T)manager.getXillaSettingsRefl().get(clazz);
    }

    public <T extends Worker> T getWorker(String name) {
        return (T)manager.getXillaWorkers().get(name);
    }

    public <T extends Worker> T getWorker(Class clazz) {
        return (T)manager.getXillaWorkersRefl().get(clazz);
    }

}
