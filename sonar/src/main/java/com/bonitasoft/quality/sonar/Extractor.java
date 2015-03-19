package com.bonitasoft.quality.sonar;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Nicolas Chabanoles on 10/12/14.
 */
public class Extractor {
    public static final String CSV_FILE_EXTENSION = ".csv";
    private Sonar sonar;
    private boolean verbose = false;
    private String outputFolder = ".";

    public Extractor(Sonar sonar, boolean verbose) {
        this.sonar = sonar;
        this.verbose = verbose;
    }

    public java.util.List<Measure> extractProject(String projectKey, String[] metrics) {
        Resource resource = sonar.find(ResourceQuery.createForMetrics(projectKey, metrics));
        if(resource==null) {
            throw new IllegalStateException("Unable to find metrics for project. Make sure project name and metrics name are valid. Project:'" +projectKey+"', Metrics: '" + toCSV(metrics) +"'.");
        }

        return resource.getMeasures();

    }

    /**
     * @param items
     * @return a CSV of each item in the given array with double quotes around each.
     */
    String toCSV(String[] items) {

        if(items==null || items.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (String metric: items) {
            if(sb.length()>0) {
                sb.append(",");
            }
            sb.append("\"").append(metric).append("\"");
        }
        return sb.toString();
    }

    public void extractProjects(List<String> projectKeys, String[] metrics) {

        for (String projectKey : projectKeys) {
            if(verbose) {
                System.out.println("----------------------------------------------------------------------------------------------");
                System.out.println("-- Querying metrics for project: " + projectKey + " --");
                System.out.println("----------------------------------------------------------------------------------------------");
            }

            long start = System.currentTimeMillis();
            List<Measure> projectMetrics = extractProject(projectKey, metrics);
            long end = System.currentTimeMillis();

            writeProjectMetrics(projectKey, metrics, projectMetrics);

            if(verbose) {
                System.out.println(projectMetrics);

                System.out.println("----------------------------------------------------------------------------------------------");
                System.out.println("-- Query took (ms): " + (end - start) + " ms for project: " + projectKey + " --");
                System.out.println("----------------------------------------------------------------------------------------------");
            }
        }
    }

    private void writeProjectMetrics(String projectKey, String[] headers, List<Measure> projectMetrics) {

        ensureFileExistsWithHeaders(projectKey, headers);
        appendMetrics(projectKey, headers, projectMetrics);

    }

    private void appendMetrics(String projectKey, String[] headers, List<Measure> projectMetrics) {

        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(outputFolder, projectKey+ CSV_FILE_EXTENSION), true))){
            TreeMap<String, Double> metrics = initMetricsMap(headers);
            for (Measure measure: projectMetrics) {
                metrics.put(measure.getMetricKey(), measure.getValue());

            }
            writeToCSV(out, metrics);


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void writeToCSV(BufferedWriter out, TreeMap<String, Double> metrics) throws IOException {
        Set<Map.Entry<String, Double>> entries = metrics.entrySet();
        for(Map.Entry<String, Double> entry : entries) {
            if(entry.getValue()!=null) {
                out.write(String.valueOf(entry.getValue()));
            }
            out.write(",");
        }
        out.write("\n");

    }

    private TreeMap<String, Double> initMetricsMap(String[] headers) {
        TreeMap<String, Double> result = new TreeMap<String, Double>();
        for (String header: headers) {
            result.put(header, null);
        }
        return result;
    }

    private void ensureFileExistsWithHeaders(String projectKey, String[] headers) {

        File file = new File(outputFolder, projectKey + CSV_FILE_EXTENSION);
        if(!file.exists()) {
            try (BufferedWriter out = new BufferedWriter(new FileWriter(file))){
                    for (String header: headers) {
                        out.write(header);
                        out.write(",");
                    }
                out.write("\n");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }
}
