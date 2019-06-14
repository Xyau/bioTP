package ar.edu.itba.bio.bioTP.cli;

import ar.edu.itba.bio.bioTP.aligment.MAS;
import ar.edu.itba.bio.bioTP.blast.RemoteBlast;
import ar.edu.itba.bio.bioTP.exercise.Exercise;
import ar.edu.itba.bio.bioTP.pattern.PatternFinder;
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
                exercise = new RemoteBlast();
            }

            if (cmd.hasOption("a")) {
                exercise = new LocalMSA();
            }

            if (cmd.hasOption("r")) {
                exercise = new MAS();
            }

            if (cmd.hasOption("p")) {
                exercise = new PatternFinder();
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
        options.addOption("r", "rMsa", false, "Remote MSA");
        options.addOption("b", "blast", false, "RemoteBlast.");
        options.addOption("p", "pattern", false, "Pattern finder.");
        return options;
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("bioTP [options] [files]", options);
        System.exit(0);
    }
}
