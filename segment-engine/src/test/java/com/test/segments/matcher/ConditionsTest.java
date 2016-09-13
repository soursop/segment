package com.test.segments.matcher;

import com.test.segments.matcher.*;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by sonegy@sk.com on 2016. 2. 4..
 */
public class ConditionsTest {

    @Test
    public void testGetConditions() throws Exception {
        // Given
        Condition condition2 = Condition.of(FieldMeta.of("2", FieldMetaType.STRING, "", 1), ConditionSign.CONTAINS, "test1", 10, 1, Condition.Status.ACTIVE);
        Condition condition1 = Condition.of(FieldMeta.of("1", FieldMetaType.STRING, "", 0), ConditionSign.CONTAINS, "test1", 10, 1, Condition.Status.ACTIVE);
        Conditions conditions = Conditions.of(Conditions.Conjunction.AND,
                condition2,
                Conditions.of(Conditions.Conjunction.AND,
                        condition1
                )
        );

        // When
        List<Condition> result = conditions.getConditions();

        // Then
        assertThat("result size", result.size(), is(2));
        assertThat("first result", result.get(0), is(condition2));
        assertThat("second result", result.get(1), is(condition1));
        assertThat("duration days", conditions.getDurationDays(), is(10));

    }

}