package ar.edu.itba.bio.bioTP.sequence;

import ar.edu.itba.bio.bioTP.exercise.Exercise;
import org.biojava.nbio.core.exceptions.CompoundNotFoundException;
import org.biojava.nbio.core.sequence.DNASequence;
import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.compound.AminoAcidCompound;
import org.biojava.nbio.core.sequence.io.FastaWriterHelper;
import org.biojava.nbio.core.sequence.io.GenbankReaderHelper;
import org.biojava.nbio.core.sequence.template.Sequence;
import org.biojava.nbio.core.sequence.transcription.Frame;
import org.biojava.nbio.core.sequence.transcription.TranscriptionEngine;

import java.io.File;
import java.util.*;

public class SequenceProcessor implements Exercise {

    public void run(String[] args) {

        if (args.length < 1){
            System.out.println("You need to specify a GenBank file");
        }

        LinkedHashMap<String, ProteinSequence> proteinSequences = null;

        try {
            proteinSequences = readGenBankFile(args[0]);
        } catch (Exception e) {
            System.err.println("Could not read GenBank file");
            System.exit(1);
        }

        Map<String, List<ProteinSequence>> translated = translateSequences(proteinSequences);

        try {
            proteinSequencesToFasta(translated);
        } catch (Exception e) {
            System.err.println("Could not write Fasta file");
        }
    }

    /**
     * Reads a GenBank file and parses the protein sequences
     * @param file a GenBank file
     * @return a map with the protein sequences
     * @throws Exception if there is a problem reading the file
     */
    private static LinkedHashMap<String, ProteinSequence> readGenBankFile(String file) throws Exception {
        File proteinFile = new File(file);
        System.out.println("Reading genbank file: " + file);
        return GenbankReaderHelper.readGenbankProteinSequence(proteinFile);
    }

    private static Map<String, List<ProteinSequence>> translateSequences(LinkedHashMap<String, ProteinSequence> proteinSequences){

        Map<String, List<ProteinSequence>> translatedProteinSequences = new HashMap<>();

        System.out.println("Translating sequences...");

        for (ProteinSequence sequence : proteinSequences.values()) {

            System.out.println("Accession ID:" + sequence.getAccession());
            System.out.println("Sequence: " + sequence.getSequenceAsString());

            List<ProteinSequence> proteinSequenceList = new ArrayList<>();

            TranscriptionEngine engine = new TranscriptionEngine.Builder().build();
            DNASequence dna;

            try {
                dna = new DNASequence(sequence.getSequenceAsString());
            } catch (CompoundNotFoundException e) {
                System.err.println("Invalid sequence");
                continue;
            }

            Map<Frame, Sequence<AminoAcidCompound>> translations =
                    engine.multipleFrameTranslation(dna, Frame.getAllFrames());

            int frameNumber = 1;

            for (Map.Entry<Frame, Sequence<AminoAcidCompound>> entry : translations.entrySet()) {

                Sequence<AminoAcidCompound> protein = entry.getValue();
                System.out.println("Frame " + frameNumber++);
                System.out.println(protein);
                ProteinSequence proteinSequence;

                try {
                    proteinSequence = new ProteinSequence(protein.getSequenceAsString());
                } catch (CompoundNotFoundException e) {
                    System.err.println("Invalid sequence");
                    continue;
                }

                proteinSequence.setOriginalHeader(entry.getKey().toString());
                proteinSequenceList.add(proteinSequence);
            }
            System.out.println();
            translatedProteinSequences.put(sequence.getAccession().toString(), proteinSequenceList);
        }

        return translatedProteinSequences;
    }

    /**
     * Writes protein sequences in Fasta files.
     * @param translated a map containing the protein sequences to write
     * @throws Exception if there is a problem writing the file
     */
    private static void proteinSequencesToFasta(Map<String, List<ProteinSequence>> translated) throws Exception {

        for (Map.Entry<String, List<ProteinSequence>> entry : translated.entrySet()) {

            String accessionID = entry.getKey();
            List<ProteinSequence> proteinSequenceList = entry.getValue();

            String filename = "protein_sequence_" + accessionID + ".fasta";

            File outputFile = new File(filename);

            FastaWriterHelper.writeProteinSequence(outputFile, proteinSequenceList);

            System.out.println("Sequences writen to file " + filename);
        }
    }
}
