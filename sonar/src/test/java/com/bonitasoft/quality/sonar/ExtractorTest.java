package com.bonitasoft.quality.sonar;

import org.assertj.core.api.Assertions;

import org.junit.Test;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Nicolas Chabanoles.
 */
public class ExtractorTest {

    @Test
    public void should_create_empty_string_from_null_array() {
        String csv = new Extractor(null, false).toCSV(null);
        Assertions.assertThat(csv).isEmpty();
    }

    @Test
    public void should_create_empty_string_from_empty_array() {
        String csv = new Extractor(null, false).toCSV(new String[0]);
        Assertions.assertThat(csv).isEmpty();
    }

    @Test
    public void should_create_a_csv_with_a_single_element() {
        String csv = new Extractor(null, false).toCSV(new String[]{"valid"});
        Assertions.assertThat(csv).isEqualTo("\"valid\"");
    }

    @Test
    public void should_create_a_csv_with_quote_around_each_value() {
        String csv = new Extractor(null, false).toCSV(new String[]{"a","valid","csv"});
        Assertions.assertThat(csv).isEqualTo("\"a\",\"valid\",\"csv\"");
    }
}
