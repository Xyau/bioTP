package ar.edu.itba.bio.bioTP.utils;

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
}
