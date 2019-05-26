import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import utils.BlastService;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class BlastMain {
    public static void main(String[] args) {
        //el path al file se va a tomar de String[] args
        final String filePath = "src/main/resources/test2.fasta";
        File fastaFile = new File(filePath);
        LinkedHashMap<String, ProteinSequence> sequences = new LinkedHashMap<>();
        try {
            System.out.println("Reading input file..");
            sequences = FastaReaderHelper.readFastaProteinSequence(fastaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new BlastService().blast(sequences);
        System.out.println("BLAST completed");
        System.exit(0);
    }
}
