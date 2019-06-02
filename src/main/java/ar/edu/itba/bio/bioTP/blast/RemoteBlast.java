package ar.edu.itba.bio.bioTP.blast;

import ar.edu.itba.bio.bioTP.exercise.Exercise;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import ar.edu.itba.bio.bioTP.utils.RemoteBlastService;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class RemoteBlast implements Exercise {
    private final static int ORF_FRAMES = 6;

    public void run(String[] args) {
        if (args.length < 2){
            System.out.println("You need to specify input file path and output file path");
            System.exit(1);
        }

        File fastaFile = new File(args[0]);
        LinkedHashMap<String, ProteinSequence> sequences = new LinkedHashMap<>();
        try {
            System.out.println("Reading input file..");
            sequences = FastaReaderHelper.readFastaProteinSequence(fastaFile);
            if (sequences.size() != ORF_FRAMES)
                throw new IllegalArgumentException("Six frames expected");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new RemoteBlastService().blast(sequences, args[1]);
        System.out.println("BLAST completed");
        System.exit(0);
    }
}
