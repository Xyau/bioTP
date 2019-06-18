package ar.edu.itba.bio.bioTP.utils;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

public class FastaUtils {
    public static LinkedHashMap<String, ProteinSequence> readFastaFile(String filePath) throws IOException {
        File fastaFile = new File(filePath);
        return FastaReaderHelper.readFastaProteinSequence(fastaFile);
    }
}
