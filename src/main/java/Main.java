import org.biojava.nbio.core.sequence.io.FastaSequenceParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        //Secuencia de prueba
        String transSeq = ">sp|P03999|OPSB_HUMAN Short-wave-sensitive opsin 1 OS=Homo sapiens OX=9606 GN=OPN1SW PE=1 SV=1\n" +
                "MRKMSEEEFYLFKNISSVGPWDGPQYHIAPVWAFYLQAAFMGTVFLIGFPLNAMVLVATL\n" +
                "RYKKLRQPLNYILVNVSFGGFLLCIFSVFPVFVASCNGYFVFGRHVCALEGFLGTVAGLV\n" +
                "TGWSLAFLAFERYIVICKPFGNFRFSSKHALTVVLATWTIGIGVSIPPFFGWSRFIPEGL\n" +
                "QCSCGPDWYTVGTKYRSESYTWFLFIFCFIVPLSLICFSYTQLLRALKAVAAQQQESATT\n" +
                "QKAEREVSRMVVVMVGSFCVCYVPYAAFAMYMVNNRNHGLDLRLVTIPSFFSKSACIYNP\n" +
                "IIYCFMNKQFQACIMKMVCGKAMTDESDTCSSQKTEVSTVSSTQVGPN";
        /**
         * Estaba tratando de hacer funcionar el parseo de biojava, pero no me funciono mucho, les dejo esto por si les
         *  sirve, pero creo que seria mas facil si pedimos las cosas de una api como hacemos en el {@link aligment.AlignmentClient}
         *  Si lo pueden hacer funcionar, borren lo que haga falta :D
         */
        FastaSequenceParser fastaSequenceParser = new FastaSequenceParser();
        //        FastaGeneWriter fastaGeneWriter = new FastaGeneWriter();
        String se= null;
        try {
            se = fastaSequenceParser.getSequence(new BufferedReader(new StringReader(transSeq)), -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(se);
//        TranscriptSequence transcriptSequence = null;
//        try {
//        } catch (CompoundNotFoundException e) {
//            e.printStackTrace();
//        }
//        GeneSequence geneSequence = new GeneSequence(chromosomeSequence, 0, chromosomeSequence.getLength(), Strand.POSITIVE);
//        TranscriptSequence transcriptSequence = new TranscriptSequence(geneSequence, 0, geneSequence.getBioEnd());
//        ProteinSequenceCreator
    }
}
