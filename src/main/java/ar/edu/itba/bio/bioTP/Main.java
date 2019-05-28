package ar.edu.itba.bio.bioTP;

import ar.edu.itba.bio.bioTP.cli.CliParser;
import ar.edu.itba.bio.bioTP.cli.Configuration;
import ar.edu.itba.bio.bioTP.exercise.Exercise;

public class Main {
    public static void main(String[] args) {
        Configuration configuration = CliParser.parse(args);
        Exercise e = configuration.getExercise();
        e.run(configuration.getArgs());
    }
}
