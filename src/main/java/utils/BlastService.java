package utils;

import java.io.BufferedReader;
import java.io.FileWriter;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.util.IOUtils;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

import java.io.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.biojava.nbio.ws.alignment.qblast.*;

public class BlastService {

    private static final String NCBI_DATA_BASE = "refseq_protein";
    private static final String BLAST_OUTPUT_FILE = "src/main/resources/output2.fasta";

    private final NCBIQBlastService ncbiBlastService = new NCBIQBlastService();
    private final NCBIQBlastAlignmentProperties searchProps = new NCBIQBlastAlignmentProperties();
    private final NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();

    public BlastService() {
        searchProps.setBlastProgram(BlastProgramEnum.blastp);
        searchProps.setBlastDatabase(NCBI_DATA_BASE);
        outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
        //ignore alignments, consider only descriptions
        outputProps.setAlignmentNumber(0);
    }

    public void blast(final Collection<ProteinSequence> sequences) {

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final List<String> responsesId = new LinkedList<>();
        final List<Callable<Boolean>> tasks = new LinkedList<>();
        for (final ProteinSequence seq : sequences)
                tasks.add(() -> responsesId.add(ncbiBlastService.sendAlignmentRequest(seq.getSequenceAsString(), searchProps)));
        try {
            //blocking call
            System.out.println("Executing BLAST..");
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Writing to output file..");
        final File output = new File(BLAST_OUTPUT_FILE);
        try {
            //clear the file if it already exists
            if (output.exists()) {
                output.delete();
                output.createNewFile();
            }
            final FileWriter writer = new FileWriter(output);
            for (final String responseId : responsesId) {
                final InputStream in = ncbiBlastService.getAlignmentResults(responseId, outputProps);
                final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null)
                    writer.write(line + System.getProperty("line.separator"));
                IOUtils.close(reader);
            }
            IOUtils.close(writer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}

