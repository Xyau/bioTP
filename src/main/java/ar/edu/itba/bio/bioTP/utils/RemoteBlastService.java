package ar.edu.itba.bio.bioTP.utils;

import java.io.BufferedReader;
import java.io.FileWriter;

import org.biojava.nbio.core.sequence.ProteinSequence;
import org.biojava.nbio.core.sequence.io.util.IOUtils;
import org.biojava.nbio.ws.alignment.qblast.NCBIQBlastService;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.biojava.nbio.ws.alignment.qblast.*;

public class RemoteBlastService {

    private static final String NCBI_DATA_BASE = "refseq_protein";

    private final NCBIQBlastService ncbiBlastService = new NCBIQBlastService();
    private final NCBIQBlastAlignmentProperties searchProps = new NCBIQBlastAlignmentProperties();
    private final NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();

    public RemoteBlastService() {
        searchProps.setBlastProgram(BlastProgramEnum.blastp);
        searchProps.setBlastDatabase(NCBI_DATA_BASE);
        outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
        //ignore alignments, consider only descriptions
        outputProps.setAlignmentNumber(0);
    }

    public void blast(final LinkedHashMap<String, ProteinSequence> sequences, final String outputFilePath) {

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

        try {
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
            //clear the file if it already exists
            final File output = new File(outputFilePath);
            if (output.exists()) {
                output.delete();
                output.createNewFile();
            }
            final FileWriter writer = new FileWriter(outputFilePath);
            writer.write(findOptimalORF(resultsList));
            IOUtils.close(writer);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String findOptimalORF(final List<InputStream> resultsList) throws IOException {
        int maxLength = 0;
        String ans = null;
        for (final InputStream in : resultsList) {
            final String s = convert(in);
            if (s.length() > maxLength) {
                maxLength = s.length();
                ans = s;
            }
        }
        return ans;
    }

    private String convert(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, Charset.defaultCharset()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}

