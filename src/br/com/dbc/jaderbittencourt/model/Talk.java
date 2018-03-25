package br.com.dbc.jaderbittencourt.model;

import java.time.LocalTime;

public class Talk {

    String title;
    int duration;
    LocalTime scheduledTime;

    public Talk() {}

    public Talk(String title, int duration, LocalTime scheduledTime) {
        this.title = title;
        this.duration = duration;
        this.scheduledTime = scheduledTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public LocalTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public void setDuration(String duration) {
        // a lightning talk has only 5 minutes
        if (duration.equals("lightning")) {
            this.duration = 5;
        } else {
            // every other talk has his duration in minutes defined
            this.duration = Integer.parseInt(duration.replace("min", ""));
        }
    }

    @Override
    public String toString() {
        return getScheduledTime().toString() + " - " + getTitle();
    }
}
