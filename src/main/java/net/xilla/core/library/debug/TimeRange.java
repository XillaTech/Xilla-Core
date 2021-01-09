package net.xilla.core.library.debug;

import lombok.Getter;

public class TimeRange {

    @Getter
    private String id;

    @Getter
    private long takenAt;

    @Getter
    private long took;

    public TimeRange(String id) {
        this.id = id;
        this.takenAt = System.currentTimeMillis();
    }

    public void stop() {
        took = System.currentTimeMillis() - takenAt;
    }

}
