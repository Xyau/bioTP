package ar.edu.itba.bio.bioTP.aligment;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class AlignmentClient {
    static String PARAMS_URL = "https://www.ebi.ac.uk/Tools/services/rest/clustalo/parameters";
    static String RUN_URL = "https://www.ebi.ac.uk/Tools/services/rest/clustalo/run";
    static String STATUS_URL = "https://www.ebi.ac.uk/Tools/services/rest/clustalo/status/";
    static String RESULTS_URL = "https://www.ebi.ac.uk/Tools/services/rest/clustalo/result/";
    static String FINISHED = "FINISHED";

    private Client client;
    private String jobId;

    public AlignmentClient() {
        this.client = ClientBuilder.newClient();
    }

    public String runAlignJob(String fastaString){
        MultivaluedMap<String, String> form = new MultivaluedHashMap<>();
        form.add("email", "julian@benitez.nu");
        form.add("sequence", fastaString);
        Response response = client.target(RUN_URL)
                .request(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_PLAIN)
                .post(Entity.form(form));
        jobId = response.readEntity(String.class);
        return jobId;
    }

    public String waitForJobToFinish(){
        Integer tries = 0;
        Integer milisBetweenTries = 50;
        String status;
        do{
            status = getJobStatus();
            System.out.println("Try #" + tries++ + ", Alignment status:" + status);
            try {
                Thread.sleep(milisBetweenTries);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!FINISHED.equals(status));
        return status;
    }

    private String getJobStatus(){
        Response response = client.target(STATUS_URL + jobId)
                .request()
                .accept(MediaType.TEXT_PLAIN)
                .get();

        return response.readEntity(String.class);
    }

    private String getJobOutput(String outputType){
        Response response = client.target(buildResultsUrl(jobId, outputType))
                .request()
                .accept(MediaType.TEXT_PLAIN)
                .get();

        return response.readEntity(String.class);
    }

    public String getJobOutputStatus(){
        return getJobOutput("out");
    }

    public String getJobOutputAligment(){
        return getJobOutput("aln-clustal_num");
    }

    public String getJobOutputPhyloTree(){
        return getJobOutput("phylotree");
    }

    private static String buildResultsUrl(String jobId, String outputType){
        return RESULTS_URL + jobId + "/" + outputType;
    }
}
