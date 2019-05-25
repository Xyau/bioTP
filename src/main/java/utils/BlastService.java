package utils;

import java.io.BufferedReader;
import java.io.FileWriter;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.util.IOUtils;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

import java.io.*;
import java.util.*;
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

    public void blast(final LinkedHashMap<String, ProteinSequence> sequences) {

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        final Map<String, String> responsesKey = new HashMap<>();
        final List<Callable<Void>> tasks = new LinkedList<>();
        sequences.forEach((accessor, sequence) -> tasks.add(() -> {
            responsesKey.put(accessor,ncbiBlastService.sendAlignmentRequest(sequence.getSequenceAsString(), searchProps));
            System.out.println("Remote request for \'" + accessor + "\' was send");
            return null;
        }));

        try {
            //blocking call
            System.out.println("Sending remote requests to server..");
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        final File output = new File(BLAST_OUTPUT_FILE);
        try {
            //clear the file if it already exists
            if (output.exists()) {
                output.delete();
                output.createNewFile();
            }
            final FileWriter writer = new FileWriter(output);
            tasks.clear();
            final List<InputStream> resultsList = new LinkedList<>();
            responsesKey.forEach((accessor, responseKey) -> tasks.add(() -> {
                resultsList.add(ncbiBlastService.getAlignmentResults(responseKey, outputProps));
                System.out.println("Remote response for \'" + accessor + "\' was fetched");
                return null;
            }));

            System.out.println("Fetching remote responses from server..");
            executorService.invokeAll(tasks);

            System.out.println("Writing information to file..");
            for (final InputStream in : resultsList) {
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

