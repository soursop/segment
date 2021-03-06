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
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void testGroupBy() {
        int size = 200000;
        int range = (size / 10);
        Random generator = new Random();
        int[] list = new int[size];
        for(int i=0; i<size; i++) {
            list[i] = generator.nextInt(range);
        };
        Stopwatch stopwatch = Stopwatch.createStarted();
        Map<Integer, Integer> group = new HashMap();
        for (int id : list) {
            if (group.containsKey(id)) {
                group.put(id, group.get(id) + 1);
            } else {
                group.put(id, 1);
            }
        }
        int count = 0;
        for(Map.Entry<Integer, Integer> entry : group.entrySet()) {
            if (entry.getValue() > 10) {
                count++;
            }
        }
        System.out.println(count);
        System.out.println(group.size());
        System.out.println("took group by " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }


    @Test
    public void testStringType() {
        int repeat = 1000000000;
        testPrimitiveStringType(repeat);
//        testObjectStringType(repeat);
    }

    private void testPrimitiveStringType(int repeat) {
        Random generator = new Random();
        Stopwatch stopwatch = Stopwatch.createStarted();
        for(int j=0; j<repeat; j++) {
            String target = ("string"+j).toLowerCase();
            String source = ("string" + generator.nextInt(repeat)).toLowerCase();
            target.contains(source);
        }
        System.out.println("took string " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }

    private void testObjectStringType(int repeat) {
        Random generator = new Random();
        Stopwatch stopwatch = Stopwatch.createStarted();
        for(int j=0; j<repeat; j++) {
            LoweredString target = new LoweredString("substring"+j);
            LoweredString source = new LoweredString("substring" + generator.nextInt(repeat));
            target.contains(source);
        }
        System.out.println("took new object string " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms");
    }

    class LoweredString {
         private final String value;
        LoweredString(String value) {
            this.value = value.toLowerCase();
        }

        public boolean contains(LoweredString string) {
            return value.contains(string.getValue());
        }

        public String getValue() {
            return value;
        }
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
