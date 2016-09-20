package com.test.segments.matcher;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ConditionTest {

    @Test
    public void testMatch() throws Exception {
        // Given
        Condition of = Condition.of(FieldMeta.of("", FieldMetaType.STRING, "", 0), ConditionSign.CONTAINS, "1", 10, 1, Condition.Status.ACTIVE);
        String[] text = {"text1"};
        // When
        boolean match = of.match(text);
        // Then
        assertTrue(match);
    }

    @Test
    public void testNotExcapeMatch() throws Exception {
        // Given
        Condition of = Condition.of(FieldMeta.of("", FieldMetaType.STRING, "", 0), ConditionSign.CONTAINS, "커피/차||우유", 10, 1, Condition.Status.ACTIVE);
        String[] text = {"커피/차"};
        // When
        boolean match = of.match(text);
        // Then
        assertFalse(match);
    }

    @Test
    public void testExcapeMatch() throws Exception {
        // Given
        Condition of = Condition.of(FieldMeta.of("", FieldMetaType.STRING, "", 0), ConditionSign.CONTAINS, "커피/차||우유", 10, 1, Condition.Status.ACTIVE);
        String[] text = {DataHandler.SOURCE_HANDLER.resolve("커피/차||우유")};
        // When
        boolean match = of.match(text);
        // Then
        assertTrue(match);
    }

    @Test
    public void testORMatch() throws Exception {
        // Given
        Condition of = Condition.of(FieldMeta.of("", FieldMetaType.STRING, "", 0), ConditionSign.CONTAINS, "커피|우유", 10, 1, Condition.Status.ACTIVE);
        String[] text1 = {"우유 텍스트"};
        String[] text2 = {"커피 텍스트"};
        // When
        boolean match1 = of.match(text1);
        boolean match2 = of.match(text2);
        // Then
        assertTrue(match1 && match2);
    }

    @Test
    public void testMatchTypeError() throws Exception {
        // Given
        Condition of = Condition.of(FieldMeta.of("", FieldMetaType.STRING, "", 0), ConditionSign.LESS_THAN, "1", 10, 1, Condition.Status.ACTIVE);
        String[] text1 = {"text1"};
        // When
        boolean match1 = of.match(text1);
        // Then
        assertFalse(match1);
    }

    @Test
    public void testMatchLessThan() throws Exception {
        // Given
        Condition of = Condition.of(FieldMeta.of("", FieldMetaType.INTEGER, "", 0), ConditionSign.LESS_THAN, "1", 10, 1, Condition.Status.ACTIVE);
        String[] text1 = {"10"};
        String[] text2 = {"-1"};
        // When
        boolean match1 = of.match(text1);
        boolean match2 = of.match(text2);
        // Then
        assertFalse(match1);
        assertTrue(match2);
    }
}