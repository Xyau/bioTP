package ar.edu.itba.bio.bioTP.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Utils {
    public static String readFromFile(String path) {
        try {
            return Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("illegal path" + path);
        }
    }
}
