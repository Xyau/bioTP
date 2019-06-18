package ar.edu.itba.bio.bioTP.emboss;

import ar.edu.itba.bio.bioTP.exercise.Exercise;
import ar.edu.itba.bio.bioTP.utils.FastaUtils;
import ar.edu.itba.bio.bioTP.utils.Utils;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.FastaWriterHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Emboss implements Exercise {

    private final String OUT_FILE_PATH = "ej5.txt";
    private final String LONGEST_ORF_FILE_PATH = "longest.fasta";
    private final String ORF_FILE_PATH = "ej5.orf";
    private final String TRANSLATED_FILE_PATH = "translated.pep";

    @Override
    public void run(String[] args) {

        if (args.length < 1){
            System.out.println("You need to specify an input file");
            System.exit(1);
        }

        getOrf(args[0], ORF_FILE_PATH);

        String nucleotidSequence = parseLongestORF(ORF_FILE_PATH);

        writeToFastaFile(nucleotidSequence, LONGEST_ORF_FILE_PATH);

        translate(LONGEST_ORF_FILE_PATH, TRANSLATED_FILE_PATH);

        getMotifs(TRANSLATED_FILE_PATH, OUT_FILE_PATH);
    }

    private void writeToFastaFile(String nucleotidSequence, String longestOrfPath) {

        ProteinSequence sequence = null;

        try {
            sequence = new ProteinSequence(nucleotidSequence);
            sequence.setOriginalHeader("LongestORF");
        } catch (CompoundNotFoundException e) {
            System.err.println("Could not parse protein from nucleotid sequence");
            System.exit(1);
        }

        try {
            FastaWriterHelper.writeProteinSequence(new File(longestOrfPath), Collections.singletonList(sequence));
        } catch (Exception e) {
            System.err.println("Could not write to file " + longestOrfPath);
            System.exit(1);
        }
    }



    private String parseLongestORF(String orfFile) {

        LinkedHashMap<String, ProteinSequence> proteinSequences = null;

        try {
            proteinSequences = FastaUtils.readFastaFile(orfFile);
        } catch (IOException e) {
            System.err.println("Could not read fasta file " + orfFile);
            System.exit(1);
        }

        String longestSequence = "";

        for (ProteinSequence sequence : proteinSequences.values()) {

            String nucleotidSequence = sequence.getSequenceAsString();

            if (nucleotidSequence.length() > longestSequence.length()){
                longestSequence = nucleotidSequence;
            }
        }

        return longestSequence;
    }

    private void getOrf(String inputFile, String outputFile) {
        Utils.runProcess("getorf " + inputFile + " -outseq " + outputFile + " -find 3", outputFile);
    }

    private void translate(String longestOrfPath, String translatedPath) {
        Utils.runProcess("transeq " + longestOrfPath + " -outseq " + translatedPath, translatedPath);
    }

    private void getMotifs(String inputFile, String outFilePath) {
        Utils.runProcess("patmatmotifs " + inputFile + " -outfile " + outFilePath, outFilePath);
    }
}
