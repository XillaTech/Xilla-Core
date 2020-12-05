package net.xilla.core.library.worker;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectWorker<T> extends Worker {

    @Getter
    private List<T> queue;

    public abstract void processItem(T object);

    public ObjectWorker(String name, long timer) {
        super(name, timer);
        this.queue = new ArrayList<>();
    }

    @Override
    public void runWorker(long start) {
        while (true) {

            if(super.getTimer() > 0) {
                if (System.currentTimeMillis() - start >= getTimer())
                    break;
            }

            if(queue.size() == 0)
                break;

            T object = queue.remove(0);
            processItem(object);
        }
    }


    public void addObject(T object) {
        queue.add(object);
    }

    public Object getObject() {
        if(queue.size() > 0)
            return queue.remove(0);
        else return null;
    }
}
