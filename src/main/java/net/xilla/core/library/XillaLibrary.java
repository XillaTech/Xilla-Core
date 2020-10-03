package net.xilla.core.library;

/**
 * Used to provide backend tools and functions to all
 * the classes we create.
 */
public class XillaLibrary {

    public double convertToHours(long time) {
        return time / 1000.0 / 60.0 / 60.0;
    }

    public String convertToTime(double time) {
        return (int)time + "h " + (int)((time - (int)time) * 60) + "m";
    }

    public long convertToMs(int hours) {
        return hours * 1000 * 60 * 60;
    }

    public long getTime(String str) {
        long time = 0;

        for(String section : str.toLowerCase().split(",")) {
            if(section.contains("s")) {
                time += Long.parseLong(section.replace("s", "")) * 1000;
            } else if(section.contains("m")) {
                time += Long.parseLong(section.replace("m", "")) * 1000 * 60;
            } else if(section.contains("h")) {
                time += Long.parseLong(section.replace("h", "")) * 1000 * 60 * 60;
            } else if(section.contains("d")) {
                time += Long.parseLong(section.replace("d", "")) * 1000 * 60 * 50 * 24;
            } else if(section.contains("w")) {
                time += Long.parseLong(section.replace("w", "")) * 1000 * 60 * 50 * 24 * 7;
            }
        }
        return time;
    }

}
