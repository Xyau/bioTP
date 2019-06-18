package ar.edu.itba.bio.bioTP.blast;

import ar.edu.itba.bio.bioTP.exercise.Exercise;
import ar.edu.itba.bio.bioTP.utils.Utils;

public class LocalBlast implements Exercise {
    public void run(String[] args) {
        if (args.length < 3) {
            System.out.println("You need to specify input file path, output file path and db file path");
            System.exit(1);
        }
        String command = "blastp -query " + args[0] + " -db " + args[2] + " -out " + args[1];
        Utils.runProcess(command, args[1]);
    }
}
