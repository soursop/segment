package com.test.segments.matcher;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class EvaluationTest {

    @Test
    public void testContains() throws Exception {
        assertThat("", Evaluation.CONTAINS_EVAL.evaluate("TEXT", "TEXT"), is(true));
        assertThat("", Evaluation.CONTAINS_EVAL.evaluate("TEXT", "TEXT1"), is(false));
    }

    @Test
    public void testNotContains() throws Exception {
        assertThat("", Evaluation.NOT_CONTAINS_EVAL.evaluate("TEXT", "TEXT1 IS NOT"), is(true));
    }

    @Test
    public void testStartWith() throws Exception {
        assertThat("", Evaluation.START_WITH_EVAL.evaluate("start with", "start with"), is(true));
        assertThat("", Evaluation.START_WITH_EVAL.evaluate("start with", "start"), is(true));
        assertThat("", Evaluation.START_WITH_EVAL.evaluate("not start with", "start"), is(false));
    }

    @Test
    public void testEndsWith() throws Exception {
        assertThat("", Evaluation.END_WITH_EVAL.evaluate("ends with", "ends with"), is(true));
        assertThat("", Evaluation.END_WITH_EVAL.evaluate("test ends with", "ends with"), is(true));
        assertThat("", Evaluation.END_WITH_EVAL.evaluate("not ends with", "not"), is(false));
    }

    @Test
    public void testEquals() throws Exception {
        assertThat("", Evaluation.EQUALS_EVAL.evaluate("REQUEST", "REQUEST"), is(true));
        assertThat("", Evaluation.EQUALS_EVAL.evaluate("1", "1"), is(true));
        assertThat("", Evaluation.EQUALS_EVAL.evaluate("1", 1), is(true));
        assertThat("", Evaluation.EQUALS_EVAL.evaluate("1", 2), is(false));
    }

    @Test
    public void testLessThan() throws Exception {
        assertThat("", Evaluation.LESS_THAN_EVAL.evaluate("1", 0), is(false));
        assertThat("", Evaluation.LESS_THAN_EVAL.evaluate("1", 1), is(false));
        assertThat("", Evaluation.LESS_THAN_EVAL.evaluate("1", 2), is(true));
    }
    @Test
    public void testLessThanEquals() throws Exception {
        assertThat("", Evaluation.LESS_THAN_OR_EQUALS_TO_EVAL.evaluate("1", 0), is(false));
        assertThat("", Evaluation.LESS_THAN_OR_EQUALS_TO_EVAL.evaluate("1", 1), is(true));
        assertThat("", Evaluation.LESS_THAN_OR_EQUALS_TO_EVAL.evaluate("1", 2), is(true));
    }

    @Test
    public void testGreaterThan() throws Exception {
        assertThat("", Evaluation.GREATER_THAN_EVAL.evaluate("1", 0), is(true));
        assertThat("", Evaluation.GREATER_THAN_EVAL.evaluate("1", 1), is(false));
        assertThat("", Evaluation.GREATER_THAN_EVAL.evaluate("1", 2), is(false));
    }

    @Test
    public void testGreaterThanEquals() throws Exception {
        assertThat("", Evaluation.GREATER_THAN_OR_EQUALS_TO_EVAL.evaluate("1", 0), is(true));
        assertThat("", Evaluation.GREATER_THAN_OR_EQUALS_TO_EVAL.evaluate("1", 1), is(true));
        assertThat("", Evaluation.GREATER_THAN_OR_EQUALS_TO_EVAL.evaluate("1", 2), is(false));
    }
}