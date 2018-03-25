package br.com.dbc.jaderbittencourt;

import br.com.dbc.jaderbittencourt.service.TrackManagement;

public class Main {

    public static void main(String... args) {
        String filePath = "input.txt";

        TrackManagement t = new TrackManagement(filePath);
        t.run();
    }
}
