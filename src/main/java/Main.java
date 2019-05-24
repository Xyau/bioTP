import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ChromosomeSequence;
import org.biojava.nbio.core.sequence.GeneSequence;
import org.biojava.nbio.core.sequence.Strand;
import org.biojava.nbio.core.sequence.TranscriptSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompoundSet;
import org.biojava.nbio.core.sequence.compound.NucleotideCompound;
import org.biojava.nbio.core.sequence.io.FastaSequenceParser;
import org.biojava.nbio.core.sequence.io.GenericFastaHeaderParser;
import org.biojava.nbio.core.sequence.io.ProteinSequenceCreator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        System.out.println("hello yo tengo que hacer el 3er ejerciio");

        String transSeq = ">sp|P03999|OPSB_HUMAN Short-wave-sensitive opsin 1 OS=Homo sapiens OX=9606 GN=OPN1SW PE=1 SV=1\n" +
                "MRKMSEEEFYLFKNISSVGPWDGPQYHIAPVWAFYLQAAFMGTVFLIGFPLNAMVLVATL\n" +
                "RYKKLRQPLNYILVNVSFGGFLLCIFSVFPVFVASCNGYFVFGRHVCALEGFLGTVAGLV\n" +
                "TGWSLAFLAFERYIVICKPFGNFRFSSKHALTVVLATWTIGIGVSIPPFFGWSRFIPEGL\n" +
                "QCSCGPDWYTVGTKYRSESYTWFLFIFCFIVPLSLICFSYTQLLRALKAVAAQQQESATT\n" +
                "QKAEREVSRMVVVMVGSFCVCYVPYAAFAMYMVNNRNHGLDLRLVTIPSFFSKSACIYNP\n" +
                "IIYCFMNKQFQACIMKMVCGKAMTDESDTCSSQKTEVSTVSSTQVGPN";
        FastaSequenceParser fastaSequenceParser = new FastaSequenceParser();
        try {
            fastaSequenceParser.getSequence(new BufferedReader(new StringReader(transSeq)), 10);
        } catch (IOException e) {
            e.printStackTrace();
        }

        TranscriptSequence transcriptSequence = null;
//        try {
//            transcriptSequence = new TranscriptSequence();
//        } catch (CompoundNotFoundException e) {
//            e.printStackTrace();
//        }
//        GeneSequence geneSequence = new GeneSequence(chromosomeSequence, 0, chromosomeSequence.getLength(), Strand.POSITIVE);
//        TranscriptSequence transcriptSequence = new TranscriptSequence(geneSequence, 0, geneSequence.getBioEnd());
//        ProteinSequenceCreator
    }
}
