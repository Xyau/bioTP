package ar.edu.itba.bio.bioTP.cli;

import ar.edu.itba.bio.bioTP.aligment.MSA;
import ar.edu.itba.bio.bioTP.blast.LocalBlast;
import ar.edu.itba.bio.bioTP.blast.RemoteBlast;
import ar.edu.itba.bio.bioTP.emboss.Emboss;
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

            if (cmd.hasOption("lb")) {
                exercise = new LocalBlast();
            }

            if (cmd.hasOption("rb")) {
                exercise = new RemoteBlast();
            }

            if (cmd.hasOption("a")) {
                exercise = new LocalMSA();
            }

            if (cmd.hasOption("r")) {
                exercise = new MSA();
            }

            if (cmd.hasOption("p")) {
                exercise = new PatternFinder();
            }

            if (cmd.hasOption("e")) {
                exercise = new Emboss();
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
        options.addOption("lb", "lblast", false, "Local blast.");
        options.addOption("rb", "rblast", false, "Remote blast.");
        options.addOption("p", "pattern", false, "Pattern finder.");
        options.addOption("e", "emboss", false, "Emboss.");
        return options;
    }

    private static void help(Options options){
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.printHelp("bioTP [options] [files]", options);
        System.exit(0);
    }
}
