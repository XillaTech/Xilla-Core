package net.xilla.core.library.worker;

import lombok.Getter;
import net.xilla.core.library.json.XillaJson;
import net.xilla.core.library.manager.ManagerObject;
import net.xilla.core.library.manager.XillaManager;
import net.xilla.core.library.net.XillaConnection;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.util.ArrayList;

public abstract class Worker extends ManagerObject {

    private long timer;
    private long start;
    private ArrayList<Long> ticks;
    private boolean isAlive;
    private boolean isPaused;
    @Getter
    private Thread thread;

    public Worker(String name, long timer) {
        super(name, WorkerManager.getInstance());
        this.ticks = new ArrayList<>();
        this.timer = timer;
        this.start = 0;
        this.isAlive = false;
        this.isPaused = false;
        this.thread = new Thread(() -> {
            long lastLoop = 0;
            isAlive = true;
            while (true) {
                if(!isAlive)
                    break;

                if(timer > 0) {
                    if (isPaused) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                        }
                        continue;
                    }
                }

                if(timer > 0) {
                    if ((System.currentTimeMillis() - lastLoop) < timer) {
                        runWorker(System.currentTimeMillis());
                        tick();
                    }
                } else {
                    runWorker(System.currentTimeMillis());
                    tick();
                }
                if(timer > 0) {
                    try {
                        long sleep = lastLoop + timer - System.currentTimeMillis() - 1;
                        if (sleep > 0)
                            Thread.sleep(sleep);
                    } catch (InterruptedException ex) {
                    }
                }
                lastLoop = System.currentTimeMillis();
            }
            isAlive = false;
        });
    }

    public void start() {
        thread.start();
        getManager().put(this);
    }

    public abstract void runWorker(long start);

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public void stopWorker() {
        isAlive = false;
    }

    private void tick() {
        if(start == 0) {
            start = System.currentTimeMillis();
        } else {
            ticks.add(System.currentTimeMillis() - start);
            if(ticks.size() > 50)
                ticks.remove(0);
        }
    }

    public double[] getTPS() {
        ArrayList<Long> temp = new ArrayList<>(ticks);

        double totalTime = 0;
        double lastTick = 0;
        for(long tick : ticks) {
            totalTime += tick - lastTick;
            lastTick = tick;
        }

        double averageTime = totalTime / temp.size();
        return new double[] {1000 / averageTime, temp.size()};
    }

    public String getStatus() {
        double[] tpsResults = getTPS();
        return "Running at " + ((int)(tpsResults[0] * 10000) / 10000.0) + " TPS, expected TPS is " + ((int)((1000.0 / timer) * 10000) / 10000.0) + " TPS (" + (int)tpsResults[1] + " Ticks in cache)";
    }

    public boolean isWorkerAlive() {
        return isAlive;
    }

    public long getTimer() {
        return timer;
    }

    @Override
    public XillaJson getSerializedData() {
        return new XillaJson().put("status", getStatus()).put("tps", getTPS());
    }

    @Override
    public void loadSerializedData(XillaJson xillaJson) {

    }

}
