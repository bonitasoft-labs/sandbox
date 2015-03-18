package com.bonitasoft.quality.sonar;

import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by Nicolas Chabanoles on 09/12/14.
 */
public class Main {


    public static String[] METRICS;
    public static List<String> PROJECT_KEYS;
    public static String URL;
    public static String LOGIN;
    public static String PASSWORD;
    public static boolean verbose;


    public static void main(String args[]) {


        try {
            readConfigurationFromFiles();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }


        Sonar sonar = new Sonar(new HttpClient4Connector(new Host(URL, LOGIN, PASSWORD)));
            Extractor extractor = new Extractor(sonar, verbose);
            extractor.setOutputFolder("target");
            extractor.extractProjects(PROJECT_KEYS, METRICS);


    }

    private static void readConfigurationFromFiles() throws IOException {
        final Properties props = new Properties();

        final InputStream publicValues = Main.class.getClassLoader().getResourceAsStream("sonar.properties");
        final InputStream privateValues = Main.class.getClassLoader().getResourceAsStream("not-on-vcs.properties");

        props.load(publicValues);
        props.load(privateValues);

        initSonarURL(props);
        initSonarLogin(props);
        initSonarPassword(props);
        initSonarProjectsToExtract(props);
        initSonarMetricsToExtract(props);
        initExtractorVerboseMode(props);


    }

    private static void initSonarURL(Properties props) throws IOException {
        URL = props.getProperty("sonar.server.url", null);
        if(URL==null) {
            throw new IOException("No URL specified to access Sonar server: please specify URL using 'sonar.server.url' key in property file.");
        }
    }

    private static void initSonarLogin(Properties props) throws IOException {
        LOGIN = props.getProperty("sonar.server.login", null);
        if(LOGIN==null) {
            throw new IOException("No LOGIN specified to access Sonar server: please specify URL using 'sonar.server.login' key in property file.");
        }
    }

    private static void initSonarPassword(Properties props)  throws IOException {
        PASSWORD = props.getProperty("sonar.server.password", null);
        if(PASSWORD==null) {
            throw new IOException("No password specified to access Sonar server: please specify PASSWORD using 'sonar.server.password' key in property file.");
        }
    }

    private static void initSonarProjectsToExtract(Properties props)  throws IOException {
        String projectsCSV = props.getProperty("sonar.projects.to.extract", null);
        PROJECT_KEYS = Arrays.asList(projectsCSV.split(","));
        if(PROJECT_KEYS==null) {
            throw new IOException("No projects to extract specified : please specify projects using 'sonar.projects.to.extract' key in property file. A CSV value is expected.");
        }
    }

    private static void initSonarMetricsToExtract(Properties props)  throws IOException {
        String metricsCSV = props.getProperty("sonar.metrics.to.extract", null);
        METRICS = metricsCSV.split(",");
        if(METRICS==null) {
            throw new IOException("No metrics to extract specified : please specify metrics using 'sonar.metrics.to.extract' key in property file.A CSV value is expected.");
        }
    }

    private static void initExtractorVerboseMode(Properties props)  throws IOException {
        verbose = Boolean.parseBoolean(props.getProperty("extractor.verbose", "false"));
    }

}
