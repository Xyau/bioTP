package ar.edu.itba.bio.bioTP.pattern;

import ar.edu.itba.bio.bioTP.exercise.Exercise;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class PatternFinder implements Exercise {
    public void run(String[] args) {
        if (args.length < 4) {
            System.out.println("You need to specify input file path, output file path, pattern search type and pattern");
            System.exit(1);
        }

        List<String> hits = null;
        try {
            System.out.println("Reading input file..");
            BufferedReader input = new BufferedReader(new FileReader(args[0]));
            String text = org.apache.commons.io.IOUtils.toString(input);
            text = text.substring(text.indexOf("ALIGNMENTS"));
            hits = new LinkedList<>(Arrays.asList(text.split(">")));
            hits.remove(0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final List<String> matchedHits;
        switch (args[2]) {
            case "seq":
                System.out.println("Starting sequence pattern finder..");
                matchedHits = sequenceFinder(hits, args[3]);
                break;
            case "id":
                System.out.println("Starting id pattern finder..");
                matchedHits = idFinder(hits, args[3]);
                break;
            case "species":
                System.out.println("Starting species pattern finder..");
                matchedHits = speciesFinder(hits, args[3]);
                break;
            default:
                throw new UnsupportedOperationException("Pattern search type '" + args[2] + "' not supported");
        }

        System.out.println("Generating output..");
        try {
            final File output = new File(args[1]);
            if (output.exists()) {
                output.delete();
                output.createNewFile();
            }
            FileWriter writer = new FileWriter(output);

            for (final String h : matchedHits) {
                writer.write(">");
                writer.write(h);
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Pattern search completed");
    }

    private List<String> sequenceFinder(final List<String> heats, final String sequencePattern) {
        final List<String> matchedHits = new LinkedList<>();
        heats.forEach(h -> {
            final String sequence = h.substring(h.indexOf("Query"), h.length());
            final String[] lines = sequence.split("\n");
            boolean isSubject = false;
            for (final String line : lines) {
                if (line.startsWith("Sbjct"))
                    isSubject = true;
                else if (line.startsWith("Query"))
                    isSubject = false;
                if (isSubject && line.toLowerCase().contains(sequencePattern.toLowerCase()))
                    matchedHits.add(h);
            }

        });
        return matchedHits;
    }

    private List<String> idFinder(final List<String> heats, final String idPattern) {
        final List<String> matchedHits = new LinkedList<>();
        heats.forEach(h -> {
            final String[] blocks = h.split("]");
            //ignore last block (alignments), consider only headers
            for (int i = 0 ; i < blocks.length - 1 ; i++) {
                int j = 0;
                while (!Character.isLetter(blocks[i].charAt(j)))
                    j++;
                final String aux = blocks[i].substring(j, blocks[i].length());
                final String id = aux.substring(0, aux.indexOf(" "));
                if (id.toLowerCase().equals(idPattern.toLowerCase()))
                    matchedHits.add(h);
            }
        });
        return matchedHits;
    }

    private List<String> speciesFinder(List<String> heats, final String speciesPattern) {
        final List<String> matchedHits = new LinkedList<>();
        heats.forEach(h -> {
            final String[] blocks = h.split("]");
            for (int i = 0 ; i < blocks.length - 1 ; i++) {
                final String species = blocks[i].substring(blocks[i].indexOf("["), blocks[i].length());
                if (species.toLowerCase().contains(speciesPattern.toLowerCase()))
                    matchedHits.add(h);
            }
        });
        return matchedHits;
    }
}
