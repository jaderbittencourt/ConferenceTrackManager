package br.com.dbc.jaderbittencourt.service;

import br.com.dbc.jaderbittencourt.model.Talk;
import br.com.dbc.jaderbittencourt.model.Track;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TrackManagement {

    private String filePath;
    private Track track1 = new Track();
    private Track track2 = new Track();

    public TrackManagement(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Build the talk list, sort and build tracks
     */
    public void run() {
        List<Talk> talks;
        try {
            talks = buildTalksList(filePath);
            sortTalks(talks);
            buildTracks(talks);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (track1.getTalks().size() > 0 && track2.getTalks().size() > 0) {
            generateOutput();
        }
    }

    /**
     * Create an output file with the organized talks
     */
    protected void generateOutput() {
        PrintWriter writer;
        try {
            writer = new PrintWriter("conference_track_output.txt", "UTF-8");

            writer.println("Track1: ");
            for (Talk t: track1.getTalks()) {
                writer.println(t.toString());
            }

            writer.println("Track2: ");
            for (Talk t: track1.getTalks()) {
                writer.println(t.toString());
            }

            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * Order talks, from longer to shorter
     * @param talks
     */
    protected void sortTalks(List<Talk> talks) {
        talks.sort((Talk t1, Talk t2) -> {
            if (t1.getDuration() < t2.getDuration())
                return 1;
            if (t1.getDuration() > t2.getDuration())
                return -1;
            return 0;
        });
    }

    /**
     * Read the file and build a talk list to be computed and scheduled
     * @param filePath path where file should be loaded from
     * @return
     */
    protected List<Talk> buildTalksList(String filePath) throws IOException {
        List<Talk> result = new ArrayList<>();
        File f = new File(filePath);
        BufferedReader abc = new BufferedReader(new FileReader(f));

        String line;
        while((line = abc.readLine()) != null) {
            result.add(buildTalkObject(line) );
        }
        abc.close();

        return result;
    }

    /**
     * Build a Talk object, with a talk title and duration
     * @param line
     * @return
     */
    protected Talk buildTalkObject(String line) {
        // split line by whitespace
        List<String> values = Arrays.asList(line.split(" "));

        // get talk duration
        String duration = values.get(values.size() -1);

        Talk talk = new Talk();
        talk.setDuration(duration);
        // remove talk duration from talk title
        talk.setTitle(line.replace(duration, ""));

        return talk;
    }

    protected void buildTracks(List<Talk> talks) {
        // fill morning agenda for both tracks
        fillMorningSession(talks, track1);
        fillMorningSession(talks, track2);

        // add lunch time for both tracks
        addLunchTime(track1);
        addLunchTime(track2);

        // fill afternoon agenda for both tracks
        fillAfternoonSession(talks, track1);
        fillAfternoonSession(talks, track2);

        // add networking event for both tracks
        addNetworkingEvent(track1);
        addNetworkingEvent(track2);
    }

    /**
     * Consumes all available minutes in the morning agenda, leaving no gaps
     * @param talks
     * @param track
     */
    protected void fillMorningSession(List<Talk> talks, Track track) {
        // while there's time left in the morning agenda, this method will look for talks to fill the agenda, over and over again.
        while (track.getMorningTimeLeft() > 0) {
            for (Iterator<Talk> iterator = talks.listIterator(); iterator.hasNext(); ) {
                Talk t = iterator.next();
                if (track.getMorningTimeLeft() % t.getDuration() == 0 ) {
                    t.setScheduledTime(LocalTime.of(track.getMorningLength() / 60, track.getMorningLength() % 60, 0 ));
                    track.increaseMorningLength(t.getDuration());
                    track.decreaseMorningTimeLeft(t.getDuration());
                    track.addTalk(t);
                    iterator.remove();
                }
                if (track.getMorningTimeLeft() == 0)
                    break;
            }
        }
    }

    /**
     * just schedule lunch
     * @param track
     */
    protected void addLunchTime(Track track) {
        track.addTalk(new Talk("Lunch", 60, LocalTime.of(12,00)));
        track.increaseMorningLength(60);
    }

    /**
     * Fill all available
     * @param talks
     * @param track
     */
    protected void fillAfternoonSession(List<Talk> talks, Track track) {
        // while there's still time in the afternoon agenda, this method looks for talks to fill it.
        while (track.getAfternoonTimeLeft() > 60) {
            for (Iterator<Talk> iterator = talks.listIterator(); iterator.hasNext(); ) {
                Talk t = iterator.next();

                // the talk cannot invade the networking event limit hour
                if (track.getAfternoonTimeLeft() - t.getDuration() >= 0) {
                    t.setScheduledTime(LocalTime.of(track.getAfternoonLength() / 60, track.getAfternoonLength() % 60, 0));
                    track.increaseAfternoonLength(t.getDuration());
                    track.decreaseAfternoonTimeLeft(t.getDuration());
                    track.addTalk(t);
                    iterator.remove();
                }

                // if there's no time left or no talk available, we just break the loop to exit the method
                if (track.getAfternoonTimeLeft() == 0 || !iterator.hasNext()) {
                    break;
                }
            }
        }
    }

    /**
     * just schedule the networking event
     * @param track
     */
    protected void addNetworkingEvent(Track track) {
        track.addTalk(new Talk("Networking Event", 60, LocalTime.of(track.getAfternoonLength() / 60, track.getAfternoonLength() % 60, 0)));
        track.increaseAfternoonLength(60);
    }
}
