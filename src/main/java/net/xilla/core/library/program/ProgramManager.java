package net.xilla.core.library.program;

import lombok.Getter;
import net.xilla.core.library.config.Settings;
import net.xilla.core.library.manager.Manager;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.manager.XillaManager;
import net.xilla.core.library.worker.Worker;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProgramManager extends ManagerObject {

    // Startup Sequence

    private final List<StartupProcess> startupSequence = new Vector<>();

    // Things to startup

    private final List<Settings> settings = new Vector<>();

    private final List<Manager> managers = new Vector<>();

    private final List<Worker> workers = new Vector<>();

    // Manager to manage data

    @Getter
    private final Map<String, Manager> xillaManagers = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, Settings> xillaSettings = new ConcurrentHashMap<>();

    @Getter
    private final Map<String, Worker> xillaWorkers = new ConcurrentHashMap<>();

    @Getter
    private final Map<Class<? extends Manager>, Manager> xillaManagersRefl = new ConcurrentHashMap<>();

    @Getter
    private final Map<Class<? extends Settings>, Settings> xillaSettingsRefl = new ConcurrentHashMap<>();

    @Getter
    private final Map<Class<? extends Worker>, Worker> xillaWorkersRefl = new ConcurrentHashMap<>();

    // Controller

    @Getter
    private final ProgramController controller;

    // Constructor, duh

    public ProgramManager(String name) {
        super(name, "");

        this.controller = new ProgramController(this);

        startupSequence.add(new StartupProcess("Settings", StartupPriority.CORE_SETTINGS) {
            @Override
            public void run() {
                settings.forEach((setting -> {
                    try {
                        setting.startup();
                    } catch (Exception ex) {
                        Logger.log(LogLevel.ERROR, "Settings " + setting.getKey() + " threw an error!", getClass());
                        ex.printStackTrace();
                    }
                }));
            }
        });

        startupSequence.add(new StartupProcess("Managers", StartupPriority.CORE_MANAGERS) {
            @Override
            public void run() {
                managers.forEach((manager -> {
                    try {
                        if(manager.getConfig() != null) {
                            manager.load();
                        }
                    } catch (Exception ex) {
                        Logger.log(LogLevel.ERROR, "Manager " + manager.getKey() + " threw an error!", getClass());
                        ex.printStackTrace();
                    }
                }));
            }
        });

        startupSequence.add(new StartupProcess("Workers", StartupPriority.CORE_WORKERS) {
            @Override
            public void run() {
                workers.forEach((worker -> {
                    try {
                        worker.start();
                    } catch (Exception ex) {
                        Logger.log(LogLevel.ERROR, "Worker " + worker.getKey() + " threw an error!", getClass());
                        ex.printStackTrace();
                    }
                }));
            }
        });
    };

    // Methods to register stuff

    public void registerManager(Manager manager) {
        this.xillaManagers.put(manager.getKey().toString(), manager);
        this.xillaManagersRefl.put(manager.getClass(), manager);
        managers.add(manager);
    }

    public void registerManager(int i, Manager manager) {
        this.xillaManagers.put(manager.getKey().toString(), manager);
        this.xillaManagersRefl.put(manager.getClass(), manager);
        this.managers.add(i, manager);
    }

    public void registerSettings(Settings settings) {
        this.xillaSettings.put(settings.getKey().toString(), settings);
        this.xillaSettingsRefl.put(settings.getClass(), settings);
        this.settings.add(settings);
    }

    public void registerSettings(int i, Settings settings) {
        this.xillaSettings.put(settings.getKey().toString(), settings);
        this.xillaSettingsRefl.put(settings.getClass(), settings);
        this.settings.add(i, settings);
    }

    public void registerWorker(Worker worker) {
        this.xillaWorkers.put(worker.getKey().toString(), worker);
        this.xillaWorkersRefl.put(worker.getClass(), worker);
        this.workers.add(worker);
    }

    public void registerWorker(int i, Worker worker) {
        this.xillaWorkers.put(worker.getKey().toString(), worker);
        this.xillaWorkersRefl.put(worker.getClass(), worker);
        this.workers.add(i, worker);
    }

    public void registerStartupProcess(StartupProcess process) {
        startupSequence.add(process);
    }

    public void registerStartupProcess(int i, StartupProcess process) {
        startupSequence.add(i, process);
    }

    // Actually starting the dang thing

    public void startup() {
        List<StartupProcess> queue = new ArrayList<>(startupSequence);

        queue.sort(Comparator.comparingInt(o -> o.getPriority().ordinal()));

        queue.forEach((q) -> {
            Logger.log(LogLevel.DEBUG, "Running startup item " + q.getKey() + " with priority " + q.getPriority(), getClass());
            q.run();
        });
    }

}
