package com.test.segments.matcher;

import com.google.common.base.Stopwatch;
import com.google.common.io.Files;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by 1002707 on 2016. 9. 12..
 */
public class PerformanceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PerformanceTest.class);


    @Test
    public void testListAndArrray() {

        int repeat = 1000;
        List<String> strings = new ArrayList<>();
        for(int i=0; i<600; i++) {
            strings.add("str" + i);
        }

        Stopwatch stopwatch = Stopwatch.createStarted();
        String[] arr = {};

        for(int j=0; j<repeat; j++) {
            arr = convertArray(strings);
        }
        System.out.println("took convert array " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");

        stopwatch.reset();
        stopwatch.start();
        for(int j=0; j<repeat; j++) {
            for(String value : arr) {
                if (value == "aa") {

                } else {

                }
//            System.out.println(String.format("time(%d) :%d", j, diff));
            }
        }
        stopwatch.stop();
        System.out.println("took array " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");

        stopwatch.reset();
        stopwatch.start();

        for(int j=0; j<repeat; j++) {
            for(String value : strings) {
                if (value == "aa") {

                } else {

                }
//            System.out.println(String.format("time(%d) :%d", j, diff));
            }
        }
        System.out.println("took list " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }

    private String[] convertArray(List<String> strings) {
        return strings.toArray(new String[strings.size()]);
    }

    @Test
    public void test() throws ParameterParsingException {
        File resource = new File(getClass().getResource("/conditions").getFile());
        if(resource.exists()) {
            try {
                List<String> lines = Files.readLines(resource, Charset.defaultCharset());
                for(String json : lines) {
                    try {
                        Conditions conditions = ParameterParser.parseConditions((JSONObject) JSONValue.parse(json));
                        System.out.println(conditions.getId());
                    } catch (Throwable ignore) {
                        LOGGER.error(String.format("ParameterParser.parseConditions(%s)", json), ignore);
                    }
                }
            } catch (IOException ignore) {
                LOGGER.error(ignore.getMessage(), ignore);
            }
        }
    }
}
