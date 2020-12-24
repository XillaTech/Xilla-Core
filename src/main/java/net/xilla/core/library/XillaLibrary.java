package net.xilla.core.library;

/**
 * Used to provide backend tools and functions to all
 * the classes we create.
 */
public interface XillaLibrary {

    default double convertToHours(long time) {
        return time / 1000.0 / 60.0 / 60.0;
    }

    default String convertToTime(long time) {
        long seconds = time / 1000;

        long minutes = seconds / 60;
        seconds -= minutes * 60;

        long hours = minutes / 60;
        minutes -= hours * 60;

        long days = hours / 24;
        hours -= days * 24;

        long weeks = days / 7;
        days -= weeks * 7;

        String response;

        if(seconds == 1) {
            response = "1 Second";
        } else {
            response = seconds + " Seconds";
        }

        if(minutes == 1) {
            response = minutes + " Minute, " + response;
        } else if(minutes > 0) {
            response = minutes + " Minutes, " + response;
        }

        if(hours == 1) {
            response = hours + " Hour, " + response;
        } else if(hours > 0) {
            response = hours + " Hours, " + response;
        }

        if(days == 1) {
            response = days + " Day, " + response;
        } else if(days > 0) {
            response = days + " Days, " + response;
        }

        if(weeks == 1) {
            response = weeks + " Week, " + response;
        } else if(weeks > 0) {
            response = weeks + " Weeks, " + response;
        }

        return response;
    }

    default long convertToMs(int hours) {
        return (long) hours * 1000 * 60 * 60;
    }

    default long getTime(String str) {
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
