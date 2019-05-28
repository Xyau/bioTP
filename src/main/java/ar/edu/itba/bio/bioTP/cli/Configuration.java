package ar.edu.itba.bio.bioTP.cli;

import ar.edu.itba.bio.bioTP.exercise.Exercise;

public class Configuration {

    private Exercise exercise;
    private String[] args;

    public Configuration(Exercise exercise, String[] args) {
        this.exercise = exercise;
        this.args = args;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public String[] getArgs() {
        return args;
    }
}
