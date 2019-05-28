package ar.edu.itba.bio.bioTP.cli;

import ar.edu.itba.bio.bioTP.blast.Blast;
import ar.edu.itba.bio.bioTP.exercise.Exercise;
import ar.edu.itba.bio.bioTP.sequence.SequenceProcessor;
import ar.edu.itba.bio.bioTP.aligment.LocalMSA;
import org.apache.commons.cli.*;

public class CliParser {

    private static Exercise exercise;

    public static Configuration parse(String[] args) {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                help(options);
            }

            if (cmd.hasOption("s")) {
                exercise = new SequenceProcessor();
            }

            if (cmd.hasOption("b")) {
                exercise = new Blast();
            }

            if (cmd.hasOption("a")) {
                exercise = new LocalMSA();
            }

            if (exercise == null){
                System.out.println("You must select an exercise.");
                help(options);
            }

            return new Configuration(exercise, cmd.getArgs());

        } catch (ParseException e) {
            System.out.println("Command not recognized.");
            help(options);
        }

        return null;
    }

    private static Options createOptions(){
        Options options = new Options();
        options.addOption("h", "help", false, "Prints this message.");
        options.addOption("s", "sequence", false, "Sequence processor.");
        options.addOption("a", "msa", false, "MSA.");
        options.addOption("b", "blast", false, "Blast.");
        return options;
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("bioTP [options] [files]", options);
        System.exit(0);
    }
}
