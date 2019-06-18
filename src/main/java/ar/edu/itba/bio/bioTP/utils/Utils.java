package ar.edu.itba.bio.bioTP.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public static void runProcess(String command, String outFilePath){
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", command);

        System.out.println("Running command: " + command);

        try {

            Process process = processBuilder.start();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Output written to file " + outFilePath);
            } else {
                System.err.println("There was a problem running the command. Error code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("There was a problem running command.");
            System.exit(1);
        }
    }
}
