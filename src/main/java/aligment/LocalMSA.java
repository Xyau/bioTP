package aligment;

import org.biojava.nbio.alignment.Alignments;
import org.biojava.nbio.core.alignment.template.Profile;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;
import org.biojava.nbio.core.util.ConcurrencyTools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class LocalMSA {

    public static void main(String[] args) {

        if (args.length < 1){
            System.out.println("You need to specify a Fasta file.");
            System.exit(1);
        }

        LinkedHashMap<String, ProteinSequence> proteinSequences = null;
        try {
            proteinSequences = readFastaFile(args[0]);
        } catch (IOException e) {
            System.err.println("Could not read Fasta file.");
            System.exit(1);
        }

        for (ProteinSequence sequence : proteinSequences.values()) {
            System.out.println("Accession ID:" + sequence.getAccession());
            System.out.println(sequence.getSequenceAsString());
        }

        multipleSequenceAlignment(proteinSequences);
    }

    private static void multipleSequenceAlignment(LinkedHashMap<String, ProteinSequence> proteinSequences) {
        Profile<ProteinSequence, AminoAcidCompound> profile =
                Alignments.getMultipleSequenceAlignment(new ArrayList<>(proteinSequences.values()));

        System.out.printf("Clustalw Alignment:%n%s%n", profile);
        ConcurrencyTools.shutdown();
    }

    private static LinkedHashMap<String, ProteinSequence> readFastaFile(String filePath) throws IOException {
        File fastaFile = new File(filePath);
        return FastaReaderHelper.readFastaProteinSequence(fastaFile);
    }
}
