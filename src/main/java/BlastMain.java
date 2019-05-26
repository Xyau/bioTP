import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import utils.BlastService;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class BlastMain {
    public static void main(String[] args) {

        if (args.length < 2){
            System.out.println("You need to specify input file path and output file path");
            System.exit(1);
        }

        File fastaFile = new File(args[0]);
        LinkedHashMap<String, ProteinSequence> sequences = new LinkedHashMap<>();
        try {
            System.out.println("Reading input file..");
            sequences = FastaReaderHelper.readFastaProteinSequence(fastaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new BlastService().blast(sequences, args[1]);
        System.out.println("BLAST completed");
        System.exit(0);
    }
}
