package com.test.segments.matcher;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1002707 on 2016. 9. 12..
 */
public class PerformanceTest {

    @Test
    public void testListAndArrray() {

        int repeat = 100;
        List<String> strings = new ArrayList<>();
        for(int i=0; i<60; i++) {
            strings.add("str" + i);
        }
        String[] arr = strings.toArray(new String[strings.size()]);
        for(int i=0; i<repeat; i++) {

            Stopwatch stopwatch = Stopwatch.createStarted();
//            System.out.println(String.format("time(%d) :%d", j, diff));
        }
    }
}
