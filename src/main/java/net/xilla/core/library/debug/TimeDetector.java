package net.xilla.core.library.debug;

import lombok.Getter;
import net.xilla.core.log.LogLevel;
import net.xilla.core.log.Logger;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TimeDetector {

    @Getter
    private long start;

    @Getter
    private TimeRange lastCheck = null;

    private List<TimeRange> ranges = new ArrayList<>();

    private DecimalFormat df = new DecimalFormat("###,###.##");

    public TimeDetector() {
        this.start = System.currentTimeMillis();
    }

    public void log(String label) {
        if(lastCheck != null) {
            lastCheck.stop();
            Logger.log(LogLevel.INFO, label + " took a total of " + df.format(lastCheck.getTook() / 1000.0) + "s.", getClass());
        }
        lastCheck = new TimeRange(label);
        ranges.add(lastCheck);
    }

}
