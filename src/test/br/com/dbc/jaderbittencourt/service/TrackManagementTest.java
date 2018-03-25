package br.com.dbc.jaderbittencourt.service;

import br.com.dbc.jaderbittencourt.model.Talk;
import br.com.dbc.jaderbittencourt.model.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackManagementTest {

    TrackManagement trackManagement;
    String filePath;

    @BeforeEach
    public void setup() {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("input.txt").getFile());
        filePath = file.getAbsolutePath();
        trackManagement = new TrackManagement(filePath);
    }

    @Test
    public void shouldAddNetworkingEvent() {
        Track t = new Track();
        trackManagement.addNetworkingEvent(t);

        Assertions.assertEquals(1, t.getTalks().size());
        Assertions.assertEquals("Networking Event", t.getTalks().get(0).getTitle());
    }

    @Test
    public void shouldAddLunchEvent() {
        Track t = new Track();
        trackManagement.addLunchTime(t);

        Assertions.assertEquals(1, t.getTalks().size());
        Assertions.assertEquals("Lunch", t.getTalks().get(0).getTitle());
    }

    @Test
    public void shouldSortTalks() {
        List<Talk> talks = new ArrayList<>();
        talks.add(new Talk("talk1", 5, null));
        talks.add(new Talk("talk2", 15, null));
        talks.add(new Talk("talk3", 30, null));
        talks.add(new Talk("talk4", 60, null));

        trackManagement.sortTalks(talks);

        Assertions.assertEquals(60, talks.get(0).getDuration());
        Assertions.assertEquals(5, talks.get(3).getDuration());
    }

    @Test
    public void shouldBuildTalksList() throws IOException {
        List<Talk> result = trackManagement.buildTalksList(filePath);

        Assertions.assertTrue(result.size() > 0);
    }

    @Test
    public void shouldFillMorningSession() {
        Track t = new Track();
        List<Talk> talks = new ArrayList<>();
        talks.add(new Talk("talk1", 60, null));
        talks.add(new Talk("talk2", 60, null));
        talks.add(new Talk("talk3", 60, null));

        trackManagement.fillMorningSession(talks, t);

        Assertions.assertEquals(0, t.getMorningTimeLeft());
        Assertions.assertEquals(3, t.getTalks().size());
    }

    @Test
    public void shouldFillAfternoonSession() {
        Track t = new Track();
        List<Talk> talks = new ArrayList<>();
        talks.add(new Talk("talk1", 60, null));
        talks.add(new Talk("talk2", 60, null));
        talks.add(new Talk("talk3", 60, null));
        talks.add(new Talk("talk4", 60, null));
        talks.add(new Talk("lightning talk", 5, null));

        trackManagement.fillAfternoonSession(talks, t);

        Assertions.assertEquals(0, t.getAfternoonTimeLeft());
        Assertions.assertEquals(4, t.getTalks().size());
        Assertions.assertEquals(1, talks.size());
    }

    @Test
    public void shouldBuildTalkObject() {
        String line = "Ruby on Rails 60min";
        Talk t = trackManagement.buildTalkObject(line);

        Assertions.assertEquals(60, t.getDuration());
        Assertions.assertEquals("Ruby on Rails ", t.getTitle());
    }

    @Test
    public void shouldReadFileAndPopulateTracks() {
        trackManagement.run();
        Assertions.assertTrue(trackManagement.getTrack1().getTalks().size() > 0);
        Assertions.assertTrue(trackManagement.getTrack2().getTalks().size() > 0);
        File f = new File("conference_track_output.txt");

        Assertions.assertTrue(f.exists());
        f.delete();
    }

}