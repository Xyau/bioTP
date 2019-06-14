package ar.edu.itba.bio.bioTP.pattern;

import ar.edu.itba.bio.bioTP.exercise.Exercise;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class patternFinder implements Exercise {
    public void run(String[] args) {
        if (args.length < 4) {
            System.out.println("You need to specify input file path, output file path, pattern search type and pattern");
            System.exit(1);
        }

        String[] hits = null;
        try {
            System.out.println("Reading input file..");
            BufferedReader input = new BufferedReader(new FileReader(args[0]));
            String text = org.apache.commons.io.IOUtils.toString(input);
            text = text.substring(text.indexOf("ALIGNMENTS"));
            hits = text.split(">");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final List<String> matchedHits;
        switch (args[3]) {
            case "seq":
                System.out.println("Starting sequence pattern finder..");
                matchedHits = sequenceFinder(hits, args[4]);
                break;
            case "id":
                System.out.println("Starting id pattern finder..");
                matchedHits = idFinder(hits, args[4]);
                break;
            case "species":
                System.out.println("Starting species pattern finder..");
                matchedHits = speciesFinder(hits, args[4]);
                break;
            default:
                throw new UnsupportedOperationException("Pattern search type '" + args[4] + "' not supported");
        }

        System.out.println("Generating output..");
        FileWriter writer = null;
        try {
            final File output = new File(args[2]);
            if (output.exists()) {
                output.delete();
                output.createNewFile();
            }
            writer = new FileWriter(output);

            for (final String h : matchedHits) {
                writer.write(">");
                writer.write(h);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Pattern search completed");
    }

    private List<String> sequenceFinder(final String[] heats, final String sequencePattern) {
        final List<String> matchedHits = new LinkedList<>();
        Arrays.stream(heats).forEach(h -> {
            final String sequence = h.substring(h.indexOf("Query"));
            final String[] lines = sequence.split("\n");
            boolean isSubject = false;
            for (final String line : lines) {
                if (line.startsWith("Sbjct"))
                    isSubject = true;
                else if (line.startsWith("Query"))
                    isSubject = false;
                if (isSubject && line.contains(sequencePattern))
                    matchedHits.add(h);
            }

        });
        return matchedHits;
    }

    private List<String> idFinder(final String[] heats, final String idPattern) {
        final List<String> matchedHits = new LinkedList<>();
        Arrays.stream(heats).forEach(h -> {
            final String id = h.substring(0, h.indexOf(".") - 1);
            if (id.equals(idPattern))
                matchedHits.add(id);
        });
        return matchedHits;
    }

    private List<String> speciesFinder(final String[] heats, final String speciesPattern) {
        final List<String> matchedHits = new LinkedList<>();
        Arrays.stream(heats).forEach(h -> {
            final String species = h.substring(h.indexOf("["), h.indexOf("]"));
            if (species.contains(speciesPattern))
                matchedHits.add(species);
        });
        return matchedHits;
    }
}
