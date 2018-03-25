package br.com.dbc.jaderbittencourt.model;

import br.com.dbc.jaderbittencourt.model.Talk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Track {

    int morningLength = 540;
    int afternoonLength = 780;

    int morningTimeLeft = 180;
    int afternoonTimeLeft = 240;

    List<Talk> talks = new ArrayList<>();

    public Track() {
        talks = new ArrayList<>();
    }

    public int getMorningLength() {
        return morningLength;
    }

    public void increaseMorningLength(int morningLength) {
        this.morningLength += morningLength;
    }

    public int getAfternoonLength() {
        return afternoonLength;
    }

    public void increaseAfternoonLength(int afternoonLength) {
        this.afternoonLength += afternoonLength;
    }

    public int getMorningTimeLeft() {
        return morningTimeLeft;
    }

    public void decreaseMorningTimeLeft(int morningTimeLeft) {
        this.morningTimeLeft -= morningTimeLeft;
    }

    public int getAfternoonTimeLeft() {
        return afternoonTimeLeft;
    }

    public void decreaseAfternoonTimeLeft(int afternoonTimeLeft) {
        this.afternoonTimeLeft -= afternoonTimeLeft;
    }

    public void addTalk(Talk talk) {
        if (Objects.isNull(this.talks))
            this.talks = new ArrayList<>();
        this.talks.add(talk);
    }

    public List<Talk> getTalks() {
        return talks;
    }
}
