package aligment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import static utils.Utils.readFromFile;

public class MAS {
    static String MALIGN = "./src/main/resources/malign.fa";
//curl -X POST --header 'Content-Type: application/x-www-form-urlencoded' --header 'Accept: text/plain' -d 'email=julian%40benitez.nu&title=Julian&guidetreeout=false&dismatout=true&dealign=true&mbed=true&mbediteration=true&iterations=3&gtiterations=0&hmmiterations=1&outfmt=fa&stype=dna&sequence=%3EMUT1%0AtagtgtgcTggggaacgaggcttGTcttctacacaCcAGTCccaagacccgccgggaggc%0AagaACACTAggacc%0A%3EMUT2%0AtagtgtgcggggAaacgaggcttcttcTGGtacaCCGGCcaACAcccaagacccgccggg%0AaggcagAATTAGaggacc%0A%3EMUT3%0AtagtgtgcggggaTTATCCacgaCgTTCAACATGgcttctAtcGTCtacacacccaagac%0Accgccgggaggcagaggacc' 'https://www.ebi.ac.uk/Tools/services/rest/clustalo/run'
    public static void main(String[] args) {
        String malign = readFromFile(MALIGN);
        AlignmentClient alignmentClient = new AlignmentClient();
        System.out.println("\nRequesting alignment:\n" + malign);
        String jobId = alignmentClient.runAlignJob(malign);
        System.out.println("Align requested, jobId:" + jobId);

        System.out.println("\nWaiting for jobId:" + jobId + " to finish...");
        String status = alignmentClient.waitForJobToFinish();
        System.out.println("JobId:" + jobId + " finished, status: " + status);

        System.out.println("\nRetrieving Job output status...");
        String out = alignmentClient.getJobOutputStatus();
        System.out.println("Job out: \n" + out);

        System.out.println("\nRetrieving alignment...");
        String align = alignmentClient.getJobOutputAligment();
        System.out.println("Job alignment: \n" + align);

        System.out.println("\nRetrieving phylo tree...");
        String tree = alignmentClient.getJobOutputPhyloTree();
        System.out.println("Job phylo tree: \n" + align);
    }
}
