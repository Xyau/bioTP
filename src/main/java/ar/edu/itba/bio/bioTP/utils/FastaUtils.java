package ar.edu.itba.bio.bioTP.utils;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaReaderHelper;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class FastaUtils {
    public static String extractBody(String fasta){
        Scanner scanner = new Scanner(fasta);
        scanner.nextLine();

        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()){
            sb.append(scanner.nextLine());
        }
        return sb.toString();
    }

    public static String extractHeader(String fasta){
        Scanner scanner = new Scanner(fasta);
        return scanner.nextLine();
    }

    public static LinkedHashMap<String, ProteinSequence> readFastaFile(String filePath) throws IOException {
        File fastaFile = new File(filePath);
        return FastaReaderHelper.readFastaProteinSequence(fastaFile);
    }
}
